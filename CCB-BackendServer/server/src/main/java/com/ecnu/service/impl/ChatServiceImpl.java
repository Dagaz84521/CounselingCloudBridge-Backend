package com.ecnu.service.impl;

import com.ecnu.dto.SessionRecordDTO;
import com.ecnu.entity.SessionRecord;
import com.ecnu.manager.SessionManager;
import com.ecnu.mapper.SessionRecordMapper;
import com.ecnu.service.ChatService;
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
import java.util.List;


@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private SessionManager sessionManager; // 之前定义的会话管理器

    @Autowired
    private SessionRecordMapper sessionRecordMapper;

    @Autowired
    private ObjectMapper objectMapper; // 确保已配置Jackson

    @Override
    public void registerSession(Long sessionId, WebSocketSession session) {
        sessionManager.addSession(sessionId, session);
    }

    @Override
    public void send(SessionRecordDTO dto) {

        SessionRecord sessionRecord = objectMapper.convertValue(dto, SessionRecord.class);

        sessionRecord.setCreatedAt(LocalDateTime.now());

        sessionRecordMapper.insert(sessionRecord);

        SessionRecordVO sessionRecordVO = objectMapper.convertValue(sessionRecord, SessionRecordVO.class);

        Long sessionId = dto.getSessionId();
        List<WebSocketSession> targetSessions = sessionManager.getSessions(sessionId);
        if (targetSessions == null) {
            log.warn("[消息发送失败] 找不到目标会话 sessionId={}", sessionId);
            return;
        }
        for (WebSocketSession targetSession : targetSessions) {
            if (!targetSession.isOpen()) {
                log.warn("[消息发送失败] 会话已关闭 sessionId={}", sessionId);
                sessionManager.removeSession(targetSession); // 清理无效会话
                return;
            }

            try {
                String message = objectMapper.writeValueAsString(sessionRecordVO);
                System.out.println(message);
                // 使用同步发送确保线程安全
                synchronized (targetSession) {
                    targetSession.sendMessage(new TextMessage(message));
                }
                log.debug("[消息投递成功] sessionId={} payload={}", sessionId, message);
            } catch (JsonProcessingException e) {
                log.error("[消息序列化失败] sessionId={} | 错误类型: {} | 详情: {}",
                        sessionId, e.getClass().getSimpleName(), e.getMessage());
            } catch (IOException e) {
                log.error("[消息发送IO异常] sessionId={} | 错误类型: {} | 详情: {}",
                        sessionId, e.getClass().getSimpleName(), e.getMessage());
                sessionManager.removeSession(targetSession); // 发生异常时清理会话
            }
        }
    }
}

