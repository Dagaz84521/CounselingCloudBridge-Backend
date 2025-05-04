// SessionManager.java
package com.ecnu.manager;

import com.ecnu.constant.MessageTypeConstant;
import com.ecnu.service.CounselorService;
import com.ecnu.service.SessionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Component
public class SessionManager {

    // 用户会话管理
    private final Map<Long, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    private final Map<Long, Instant> userActivities = new ConcurrentHashMap<>(); // 新增用户活动时间

    // 房间关系管理
    private final Map<Long, Instant> roomActivities = new ConcurrentHashMap<>();
    private final Map<Long, Set<Long>> roomUsers = new ConcurrentHashMap<>();
    private final Map<Long, Set<Long>> userRooms = new ConcurrentHashMap<>();
    private final Map<Long, String> roomTypes = new ConcurrentHashMap<>();

    // 超时配置（30分钟）
    private static final long ROOM_TIMEOUT_SECONDS = 10;
    private static final long CHECK_INTERVAL = 5 * 1000;

    @Resource
    private TaskScheduler taskScheduler;
    @Resource
    private SessionsService sessionsService;
    @Resource
    private CounselorService counselorService;

    private ScheduledFuture<?> timeoutTask;

    @PostConstruct
    public void init() {
        timeoutTask = taskScheduler.scheduleAtFixedRate(this::checkRoomTimeouts, CHECK_INTERVAL);
    }

    @PreDestroy
    public void destroy() {
        if (timeoutTask != null) {
            timeoutTask.cancel(true);
        }
    }

    private void checkRoomTimeouts() {
        Instant now = Instant.now();
        List<Long> expiredRooms = new ArrayList<>();

        // 检测超时房间
        roomActivities.forEach((roomId, lastActive) -> {
            if (lastActive.plusSeconds(ROOM_TIMEOUT_SECONDS).isBefore(now)) {
                expiredRooms.add(roomId);
            }
        });

        // 处理超时房间
        expiredRooms.forEach(roomId -> {
            log.info("房间 {} 因超时即将关闭", roomId);

            // 获取房间用户快照
            Set<Long> userIds = roomUsers.getOrDefault(roomId, Collections.emptySet());
            new ArrayList<>(userIds).forEach(userId -> {
                removeUserFromRoom(userId, roomId);
                checkUserConnection(userId);
            });

            // 更新数据库状态
            updateRoomStatus(roomId);
            cleanupRoom(roomId);
        });
    }

    private void checkUserConnection(Long userId) {
        if (!userRooms.containsKey(userId)) {
            WebSocketSession session = userSessions.remove(userId);
            if (session != null && session.isOpen()) {
                try {
                    session.close();
                    log.info("用户 {} 已断开连接", userId);
                } catch (IOException e) {
                    log.error("关闭连接失败", e);
                }
            }
        }
    }

    public void updateRoomActivity(Long roomId) {
        roomActivities.put(roomId, Instant.now());
    }
    public void addUserSession(Long userId, WebSocketSession rawSession) {
        WebSocketSession session = new ConcurrentWebSocketSessionDecorator(
                rawSession, 100, 1024 * 1024
        );

        Optional.ofNullable(userSessions.put(userId, session))
                .ifPresent(oldSession -> {
                    try {
                        if (oldSession.isOpen()) oldSession.close();
                    } catch (IOException e) {
                        log.error("关闭旧连接异常", e);
                    }
                });
    }

    public void addUserToRoom(Long userId, Long roomId, String type) {
        roomUsers.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(userId);
        userRooms.computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet()).add(roomId);
        roomTypes.putIfAbsent(roomId, type);
        updateRoomActivity(roomId);
    }

    private void removeUserFromRoom(Long userId, Long roomId) {
        Optional.ofNullable(roomUsers.get(roomId)).ifPresent(users -> {
            users.remove(userId);
            if (users.isEmpty()) roomUsers.remove(roomId);
        });

        Optional.ofNullable(userRooms.get(userId)).ifPresent(rooms -> {
            rooms.remove(roomId);
            if (rooms.isEmpty()) userRooms.remove(userId);
        });
    }

    private void updateRoomStatus(Long roomId) {
        String type = roomTypes.get(roomId);
        if (type == null) {
            log.warn("尝试更新未知房间的状态: {}", roomId);
            return;
        }
        try {
            switch (type) {
                case MessageTypeConstant.SESSION:
                    sessionsService.endSession(roomId, 5);
                    break;
                case MessageTypeConstant.REQUEST:
                    counselorService.endRequest(roomId);
                    break;
                default:
                    log.error("检测到非法房间类型: {} [roomId={}]", type, roomId);
            }
            log.info("房间状态已持久化 | type={} roomId={}", type, roomId);
        } catch (Exception e) {
            log.error("房间状态更新失败 | roomId={} error={}", roomId, e.getMessage());
            // 重试逻辑（可根据需要实现）
        }
    }

    private void cleanupRoom(Long roomId) {
        synchronized (this) {
            roomActivities.remove(roomId);
            roomUsers.remove(roomId);
            roomTypes.remove(roomId);

            // 同时清理反向索引中的空用户条目
            roomUsers.values().removeIf(Set::isEmpty);
            userRooms.values().removeIf(Set::isEmpty);
        }
        log.info("房间 {} 已完全清理", roomId);
    }

    public WebSocketSession getUserSession(Long userId) {
        return userSessions.get(userId);
    }

    public void removeSession(WebSocketSession session) {
        userSessions.forEach((id, sess) -> {
            if (sess == session) {
                userSessions.remove(id, sess);
                log.warn("Removed orphan session for user: {}", id);
            }
        });
    }
}
