package com.ecnu.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SessionManager {

    // 用户ID到WebSocket会话的映射
    private final Map<Long, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    // 会话装饰配置参数
    private static final int SEND_TIME_LIMIT = 1000;  // 发送时间限制（毫秒）
    private static final int BUFFER_SIZE_LIMIT = 1024 * 1024; // 缓冲区大小限制（字节）

    /**
     * 添加用户会话（自动关闭旧连接）
     * @param userId 用户唯一标识
     * @param rawSession 原始WebSocket会话
     */
    public void addUserSession(Long userId, WebSocketSession rawSession) {
        // 创建线程安全包装会话
        WebSocketSession session = new ConcurrentWebSocketSessionDecorator(
                rawSession, SEND_TIME_LIMIT, BUFFER_SIZE_LIMIT
        );

        // 保存用户ID到会话属性
        session.getAttributes().put("userId", userId);

        // 原子操作处理旧连接
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
    }

    /**
     * 获取用户会话
     * @param userId 用户唯一标识
     * @return 可能包含会话的Optional对象
     */
    public WebSocketSession getUserSession(Long userId) {
        return userSessions.get(userId);
    }

    /**
     * 移除并关闭用户会话
     * @param userId 用户唯一标识
     */
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
    }

    /**
     * 通过会话实例移除连接
     * @param targetSession 目标会话实例
     */
    public void removeSession(WebSocketSession targetSession) {
        Optional.ofNullable(targetSession.getAttributes().get("userId"))
                .map(userId -> (Long) userId)
                .ifPresent(userId -> {
                    if (userSessions.get(userId) == targetSession) {
                        removeUserSession(userId);
                    }
                });
    }

    /**
     * 获取活跃用户连接数
     */
    public int getActiveConnections() {
        return (int) userSessions.values().stream()
                .filter(WebSocketSession::isOpen)
                .count();
    }
}

