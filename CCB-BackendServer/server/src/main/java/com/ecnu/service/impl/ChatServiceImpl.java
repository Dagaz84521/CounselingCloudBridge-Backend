package com.ecnu.service.impl;

import com.ecnu.constant.SessionRecordConstant;
import com.ecnu.dto.SessionRecordDTO;
import com.ecnu.entity.Session;
import com.ecnu.entity.SessionRecord;
import com.ecnu.mapper.SessionRecordMapper;
import com.ecnu.result.Result;
import com.ecnu.service.ChatService;
import com.ecnu.service.SessionsService;
import com.ecnu.vo.SessionRecordVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    @Autowired
    private SessionRecordMapper sessionRecordMapper;

    @Autowired
    private SessionsService sessionService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Transactional
    public SessionRecordVO sendMessage(SessionRecordDTO dto) {
        // 验证会话有效性
        Session session = sessionService.validateSessionAccess(dto.getSessionId(), dto.getSenderId());

        Long receiverID = getReceiverID(session, dto.getSenderId());

        SessionRecord record = SessionRecord.builder()
                .sessionId(dto.getSessionId())
                .senderId(dto.getSenderId())
                .receiverId(receiverID)
                .status(SessionRecordConstant.SENDING)
                .content(dto.getContent())
                .anonymizedData(null)
                .createdAt(LocalDateTime.now())
                .build();

        sessionRecordMapper.insert(record);
        // 转换为VO并推送
        SessionRecordVO vo = convertToVO(record);
        messagingTemplate.convertAndSend(
                "/session/" + dto.getSessionId(),
                Result.success(vo)
        );

        // 异步更新消息状态为已送达
        updateMessageStatusAsync(record.getRecordId(), SessionRecordConstant.SENT);

        return convertToVO(record);
    }

    private Long getReceiverID(Session session, Long senderID) {
        Long clientId = session.getClientId();
        Long counselorId = session.getCounselorId();
        return Objects.equals(clientId, senderID) ? counselorId : clientId;
    }

    private SessionRecordVO convertToVO(SessionRecord message) {
        return SessionRecordVO.builder()
                .content(message.getContent())
                .senderId(message.getSenderId())
                .time(message.getCreatedAt())
                .build();
    }

    @Async
    public void updateMessageStatusAsync(Long messageId, String status) {
        sessionRecordMapper.updateRecordStatus(messageId, status);
    }

    public List<SessionRecordVO> getHistoryMessages(Long sessionId, int page, int size) {

        List<SessionRecord> messages = sessionRecordMapper.selectBySessionId(
                sessionId, page * size, size
        );

        return messages.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
}
