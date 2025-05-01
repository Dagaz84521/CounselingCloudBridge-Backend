package com.ecnu.manager;

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

    // 用户ID到WebSocket会话的映射
    private final Map<Long, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    // 用户最后活动时间记录
    private final Map<Long, Instant> lastActivityTimes = new ConcurrentHashMap<>();

    // 房间ID到用户ID集合的映射
    private final Map<Long, Set<Long>> roomUsers = new ConcurrentHashMap<>();

    // 用户ID到所在房间ID集合的映射
    private final Map<Long, Set<Long>> userRooms = new ConcurrentHashMap<>();

    // 会话装饰配置参数
    private static final int SEND_TIME_LIMIT = 1000;
    private static final int BUFFER_SIZE_LIMIT = 1024 * 1024;

    @Resource
    private TaskScheduler taskScheduler;
    private ScheduledFuture<?> timeoutTask;

    private static final long TIMEOUT_SECONDS = 60 * 30; // 多久为发送消息后会断开连接
    private static final long CHECK_INTERVAL = 5 * 1000; // 新增检查间隔（5秒）

    @PostConstruct
    public void init() {
        timeoutTask = taskScheduler.scheduleAtFixedRate(
                this::checkTimeoutConnections,
                CHECK_INTERVAL // 使用5秒间隔
        );
    }

    @PreDestroy
    public void destroy() {
        if (timeoutTask != null) {
            timeoutTask.cancel(true);
        }
    }

    private void checkTimeoutConnections() {
        Instant now = Instant.now();
        lastActivityTimes.forEach((userId, lastActive) -> {
            if (lastActive.plusSeconds(TIMEOUT_SECONDS).isBefore(now)) {
                log.info("用户{}连接超时，即将关闭", userId);
                removeUserSession(userId);
            }
        });
    }

    public void updateUserActivity(Long userId) {
        lastActivityTimes.put(userId, Instant.now());
    }

    public void addUserSession(Long userId, WebSocketSession rawSession) {
        WebSocketSession session = new ConcurrentWebSocketSessionDecorator(
                rawSession, SEND_TIME_LIMIT, BUFFER_SIZE_LIMIT
        );

        session.getAttributes().put("userId", userId);

        Optional.ofNullable(userSessions.put(userId, session))
                .ifPresent(oldSession -> {
                    try {
                        if (oldSession.isOpen()) {
                            oldSession.close();
                            log.info("关闭用户{}的旧连接", userId);
                        }
                    } catch (IOException e) {
                        log.error("关闭用户{}旧连接异常", userId, e);
                    }
                });

        updateUserActivity(userId);
    }

    public void addUserToRoom(Long userId, Long roomId) {
        roomUsers.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(userId);
        userRooms.computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet()).add(roomId);
    }

    public void removeUserFromRoom(Long userId, Long roomId) {
        Optional.ofNullable(roomUsers.get(roomId)).ifPresent(users -> users.remove(userId));
        Optional.ofNullable(userRooms.get(userId)).ifPresent(rooms -> rooms.remove(roomId));

        if (roomUsers.containsKey(roomId) && roomUsers.get(roomId).isEmpty()) {
            roomUsers.remove(roomId);
        }

        if (userRooms.containsKey(userId) && userRooms.get(userId).isEmpty()) {
            userRooms.remove(userId);
        }
    }

    public void handleUserDisconnect(Long userId) {
        Set<Long> rooms = userRooms.getOrDefault(userId, Collections.emptySet());

        rooms.forEach(roomId -> removeUserFromRoom(userId, roomId));

        rooms.forEach(roomId -> {
            Set<Long> usersInRoom = roomUsers.get(roomId);
            if (usersInRoom != null) {
                usersInRoom.forEach(otherUserId -> {
                    if (userRooms.getOrDefault(otherUserId, Collections.emptySet()).size() <= 1) {
                        removeUserSession(otherUserId);
                    }
                });
            }
        });
    }

    public WebSocketSession getUserSession(Long userId) {
        return userSessions.get(userId);
    }

    public void removeUserSession(Long userId) {
        Optional.ofNullable(userSessions.remove(userId))
                .ifPresent(session -> {
                    try {
                        if (session.isOpen()) {
                            session.close();
                        }
                    } catch (IOException e) {
                        log.error("关闭用户{}会话异常", userId, e);
                    }
                });

        handleUserDisconnect(userId);
        lastActivityTimes.remove(userId);
    }

    public void removeSession(WebSocketSession targetSession) {
        Optional.ofNullable(targetSession.getAttributes().get("userId"))
                .map(userId -> (Long) userId)
                .ifPresent(userId -> {
                    if (userSessions.get(userId) == targetSession) {
                        removeUserSession(userId);
                    }
                });
    }

    public int getActiveConnections() {
        return (int) userSessions.values().stream()
                .filter(WebSocketSession::isOpen)
                .count();
    }
}
