package com.ecnu.service.impl;

import com.ecnu.dto.MessageDTO;
import com.ecnu.entity.SessionRecord;
import com.ecnu.mapper.SessionRecordMapper;
import com.ecnu.service.SessionRecordService;
import com.ecnu.vo.SessionRecordVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionRecordServiceImpl implements SessionRecordService {

    @Autowired
    private SessionRecordMapper sessionRecordMapper;
    @Async
    public void updateMessageStatusAsync(Long messageId, String status) {
        sessionRecordMapper.updateRecordStatus(messageId, status);
    }

    @Override
    public SessionRecordVO insertSessionRecord(MessageDTO dto) {
        SessionRecord message = dtoToEntity(dto);
        sessionRecordMapper.insert(message);
        return entityToVO(message);
    }

    public List<SessionRecordVO> getHistoryMessages(Long sessionId, Long page, Long size) {

        List<SessionRecord> messages = sessionRecordMapper.selectBySessionId(
                sessionId, page * size, size
        );

        return messages.stream()
                .map(this::entityToVO)
                .collect(Collectors.toList());
    }

    private SessionRecordVO entityToVO(SessionRecord message) {
        return SessionRecordVO.builder()
                .content(message.getContent())
                .senderId(message.getSenderId())
                .createdAt(message.getCreatedAt())
                .build();
    }

    private SessionRecord dtoToEntity(MessageDTO message) {
        return SessionRecord.builder()
                .sessionId(message.getMessageId())
                .senderId(message.getSenderId())
                .receiverId(message.getReceiverId())
                .content(message.getContent())
                .build();
    }
}
