// ChatWebSocketHandler.java
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

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private ChatService chatService;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        String path = Objects.requireNonNull(session.getUri()).getPath();
        Matcher matcher = Pattern.compile("^/chat/(?<userId>\\d+)$").matcher(path);

        if (matcher.matches()) {
            try {
                Long userId = Long.parseLong(matcher.group("userId"));
                sessionManager.addUserSession(userId, session);
                log.info("用户 {} 连接建立", userId);
            } catch (NumberFormatException e) {
                log.error("非法用户ID格式", e);
                session.close();
            }
        } else {
            log.error("非法连接路径: {}", path);
            session.close();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            MessageDTO dto = objectMapper.readValue(message.getPayload(), MessageDTO.class);
            Long roomId = dto.getRoomId();

            // 更新房间活跃时间
            if (roomId != null) {
                sessionManager.updateRoomActivity(roomId);
                sessionManager.addUserToRoom(dto.getSenderId(), roomId, dto.getType());
            }

            // 消息路由
            switch (Objects.requireNonNull(dto.getType())) {
                case MessageTypeConstant.SESSION:
                    chatService.sendToSession(dto);
                    break;
                case MessageTypeConstant.REQUEST:
                    chatService.sendToRequest(dto);
                    break;
                case MessageTypeConstant.ROOM_ESTABLISHED:
                    break;
                default:
                    log.warn("未知消息类型: {}", dto.getType());
            }
        } catch (Exception e) {
            log.error("消息处理异常", e);
            throw e;
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("传输错误", exception);
        sessionManager.removeSession(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        sessionManager.removeSession(session);
        log.info("连接关闭，状态码: {}", status);
    }
}
