package com.ecnu.service.impl;

import com.ecnu.dto.MessageDTO;
import com.ecnu.entity.RequestRecord;
import com.ecnu.entity.SessionRecord;
import com.ecnu.manager.SessionManager;
import com.ecnu.mapper.RequestRecordMapper;
import com.ecnu.mapper.SessionRecordMapper;
import com.ecnu.service.ChatService;
import com.ecnu.vo.RequestRecordVO;
import com.ecnu.vo.SessionRecordVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;


@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private SessionRecordMapper sessionRecordMapper;

    @Autowired
    private RequestRecordMapper requestRecordMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void registerSession(Long senderId, WebSocketSession session) {
        sessionManager.addUserSession(senderId, session);
    }

    @Override
    public void sendToSession(MessageDTO dto)  {

        SessionRecord sessionRecord = objectMapper.convertValue(dto, SessionRecord.class);

        sessionRecord.setSessionId(dto.getRoomId());

        sessionRecord.setCreatedAt(LocalDateTime.now());

        sessionRecordMapper.insert(sessionRecord);

        SessionRecordVO sessionRecordVO = objectMapper.convertValue(sessionRecord, SessionRecordVO.class);

        Long receiverId = dto.getReceiverId();

        String message = null;

        try {
            message = objectMapper.writeValueAsString(sessionRecordVO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        this.sendToReceiver(receiverId, message);
    }

    @Override
    public void sendToRequest(MessageDTO dto) {

        RequestRecord requestRecord = objectMapper.convertValue(dto, RequestRecord.class);

        requestRecord.setRequestId(dto.getRoomId());

        requestRecord.setCreatedAt(LocalDateTime.now());

        requestRecordMapper.insert(requestRecord);

        RequestRecordVO requestRecordVO = objectMapper.convertValue(requestRecord, RequestRecordVO.class);

        Long receiverId = dto.getReceiverId();

        String message = null;

        try {
            message = objectMapper.writeValueAsString(requestRecordVO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        this.sendToReceiver(receiverId, message);
    }

    private void sendToReceiver(Long receiverId, String message) {

        WebSocketSession targetSession = sessionManager.getUserSession(receiverId);

        if (targetSession == null) {
            log.warn("[消息发送失败] 无对应会话 userId={}", receiverId);
            return;
        }
        if (!targetSession.isOpen()) {
            log.warn("[消息发送失败] 会话已关闭 userId={}", receiverId);
            sessionManager.removeSession(targetSession); // 清理无效会话
            return;
        }

        try {
            // 使用同步发送确保线程安全
            synchronized (targetSession) {
                targetSession.sendMessage(new TextMessage(message));
            }
            log.debug("[消息投递成功] receiverId={} payload={}", receiverId, message);
        } catch (JsonProcessingException e) {
            log.error("[消息序列化失败] userId={} | 错误类型: {} | 详情: {}",
                    receiverId, e.getClass().getSimpleName(), e.getMessage());
        } catch (IOException e) {
            log.error("[消息发送IO异常] userId={} | 错误类型: {} | 详情: {}",
                    receiverId, e.getClass().getSimpleName(), e.getMessage());
            sessionManager.removeSession(targetSession); // 发生异常时清理会话
        }
    }
}

