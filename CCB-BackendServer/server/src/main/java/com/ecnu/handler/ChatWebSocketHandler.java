package com.ecnu.handler;

import com.ecnu.dto.SessionRecordDTO;
import com.ecnu.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
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


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String path = Objects.requireNonNull(session.getUri()).getPath();

        Pattern chatPathPattern = Pattern.compile("^/chat/(?<senderId>\\d+)$");

        Matcher chatPathMatcher = chatPathPattern.matcher(path);

        if (chatPathMatcher.matches()) {
            String senderIdStr = chatPathMatcher.group("senderId");
            try {
                Long senderId = Long.parseLong(senderIdStr);  // 转换为 Long 类型
                System.out.println("WebSocket 连接已建立: " + senderId + " ," + session.getId());
                chatService.registerSession(senderId, session);
            } catch (NumberFormatException e) {
                System.err.println("非法的 senderId: " + senderIdStr);
                session.close();  // 关闭连接
            }
        }
    }



    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 接收到客户端消息后，解析并处理
        String payload = message.getPayload();

        SessionRecordDTO dto = objectMapper.readValue(payload, SessionRecordDTO.class);

        chatService.send(dto);

        System.out.println(payload);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        System.out.println("WebSocket 连接关闭: " + session.getId());
    }
}
