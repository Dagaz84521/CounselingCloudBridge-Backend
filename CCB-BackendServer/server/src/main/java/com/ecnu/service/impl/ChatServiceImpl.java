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
    public void sendToSession(MessageDTO dto) {
        try {
            SessionRecord sessionRecord = objectMapper.convertValue(dto, SessionRecord.class);
            sessionRecord.setSessionId(dto.getRoomId());
            sessionRecord.setCreatedAt(LocalDateTime.now());

            sessionRecordMapper.insert(sessionRecord);

            SessionRecordVO sessionRecordVO = objectMapper.convertValue(sessionRecord, SessionRecordVO.class);
            sessionRecordVO.setType(dto.getType());
            Long receiverId = dto.getReceiverId();
            String message = objectMapper.writeValueAsString(sessionRecordVO);

            this.sendToReceiver(receiverId, message);
        } catch (Exception e) {
            log.error("发送会话消息异常", e);
            throw new RuntimeException("发送会话消息失败", e);
        }
    }

    @Override
    public void sendToRequest(MessageDTO dto) {
        try {
            RequestRecord requestRecord = objectMapper.convertValue(dto, RequestRecord.class);
            requestRecord.setRequestId(dto.getRoomId());
            requestRecord.setCreatedAt(LocalDateTime.now());

            requestRecordMapper.insert(requestRecord);

            RequestRecordVO requestRecordVO = objectMapper.convertValue(requestRecord, RequestRecordVO.class);
            requestRecordVO.setType(dto.getType());
            Long receiverId = dto.getReceiverId();
            String message = objectMapper.writeValueAsString(requestRecordVO);

            this.sendToReceiver(receiverId, message);
        } catch (Exception e) {
            log.error("发送请求消息异常", e);
            throw new RuntimeException("发送请求消息失败", e);
        }
    }

    private void sendToReceiver(Long receiverId, String message) {
        try {
            WebSocketSession targetSession = sessionManager.getUserSession(receiverId);

            if (targetSession == null) {
                log.warn("[消息发送失败] 无对应会话 userId={}", receiverId);
                return;
            }

            if (!targetSession.isOpen()) {
                log.warn("[消息发送失败] 会话已关闭 userId={}", receiverId);
                sessionManager.removeSession(targetSession);
                return;
            }

            synchronized (targetSession) {
                targetSession.sendMessage(new TextMessage(message));
            }

            log.debug("[消息投递成功] receiverId={} payload={}", receiverId, message);
        } catch (JsonProcessingException e) {
            log.error("[消息序列化失败] userId={}", receiverId, e);
        } catch (IOException e) {
            log.error("[消息发送IO异常] userId={}", receiverId, e);
            sessionManager.removeSession(sessionManager.getUserSession(receiverId));
        } catch (Exception e) {
            log.error("[消息发送异常] userId={}", receiverId, e);
        }
    }
}
