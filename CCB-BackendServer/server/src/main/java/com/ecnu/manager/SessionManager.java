package com.ecnu.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
public class SessionManager {

    // 使用两级映射结构: sessionId -> 会话列表
    private final Map<Long, List<WebSocketSession>> sessionGroups = new ConcurrentHashMap<>();

    // 添加会话到指定session组
    public void addSession(Long sessionId, WebSocketSession rawSession) {
        // 包装线程安全会话
        WebSocketSession session = new ConcurrentWebSocketSessionDecorator(
                rawSession, 1000, 1024 * 1024
        );

        // 原子操作添加会话到组
        sessionGroups.compute(sessionId, (key, existingSessions) -> {
            List<WebSocketSession> sessions = existingSessions != null ?
                    existingSessions : new CopyOnWriteArrayList<>();
            sessions.add(session);
            return sessions;
        });
    }

    // 获取指定session组的所有活跃连接
    public List<WebSocketSession> getSessions(Long sessionId) {
        return sessionGroups.getOrDefault(sessionId, Collections.emptyList());
    }

    // 移除单个物理连接
    public void removeSession(WebSocketSession targetSession) {
        sessionGroups.forEach((sessionId, sessions) -> {
            boolean removed = sessions.removeIf(session ->
                    session.getId().equals(targetSession.getId())
            );

            // 如果组变空则清理映射
            if (removed && sessions.isEmpty()) {
                sessionGroups.remove(sessionId);
            }
        });
    }

}
