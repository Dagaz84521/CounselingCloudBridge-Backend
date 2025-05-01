package com.ecnu.handler;

import com.ecnu.constant.MessageTypeConstant;
import com.ecnu.dto.MessageDTO;
import com.ecnu.manager.SessionManager;
import com.ecnu.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ChatService chatService;

    @Autowired
    private SessionManager sessionManager;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        String path = Objects.requireNonNull(session.getUri()).getPath();
        Pattern chatPathPattern = Pattern.compile("^/chat/(?<senderId>\\d+)$");
        Matcher chatPathMatcher = chatPathPattern.matcher(path);

        if (chatPathMatcher.matches()) {
            String senderIdStr = chatPathMatcher.group("senderId");
            try {
                Long senderId = Long.parseLong(senderIdStr);
                log.info("WebSocket 连接已建立: {} , {}", senderId, session.getId());
                sessionManager.addUserSession(senderId, session);
            } catch (NumberFormatException e) {
                log.error("非法的 senderId: {}", senderIdStr, e);
                session.close();
            }
        } else {
            log.error("无效的WebSocket连接路径: {}", path);
            session.close();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            String payload = message.getPayload();
            MessageDTO dto = objectMapper.readValue(payload, MessageDTO.class);

            Long userId = (Long) session.getAttributes().get("userId");
            if (userId != null) {
                sessionManager.updateUserActivity(userId);

                if (dto.getRoomId() != null) {
                    sessionManager.addUserToRoom(userId, dto.getRoomId());
                }
            }

            String type = dto.getType();
            if (MessageTypeConstant.SESSION.equals(type)) {
                chatService.sendToSession(dto);
            } else if (MessageTypeConstant.REQUEST.equals(type)) {
                chatService.sendToRequest(dto);
            } else {
                log.warn("未知的消息类型: {}", type);
            }

            log.debug("收到消息: {}", payload);
        } catch (Exception e) {
            log.error("处理WebSocket消息异常", e);
            throw e;
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        try {
            Long userId = (Long) session.getAttributes().get("userId");
            if (userId != null) {
                log.info("WebSocket 连接关闭: 用户 {}, 会话 {}, 状态 {}", userId, session.getId(), status);
                sessionManager.handleUserDisconnect(userId);
                sessionManager.removeUserSession(userId);
            }
        } catch (Exception e) {
            log.error("处理WebSocket连接关闭异常", e);
            throw e;
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket传输错误: 会话 {}", session.getId(), exception);
        super.handleTransportError(session, exception);
    }
}
