package com.ecnu.service.impl;

import com.ecnu.dto.MessageDTO;
import com.ecnu.entity.SessionRecord;
import com.ecnu.entity.User;
import com.ecnu.mapper.SessionRecordMapper;
import com.ecnu.mapper.UserMapper;
import com.ecnu.service.SessionRecordService;
import com.ecnu.vo.SessionRecordVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionRecordServiceImpl implements SessionRecordService {

    @Autowired
    private SessionRecordMapper sessionRecordMapper;
    @Autowired
    private UserMapper userMapper;

    @Async
    public void updateMessageStatusAsync(Long messageId, String status) {
        sessionRecordMapper.updateRecordStatus(messageId, status);
    }


    public List<SessionRecordVO> getHistoryMessages(Long sessionId, Long page, Long size) {

        List<SessionRecord> messages = sessionRecordMapper.selectBySessionId(
                sessionId, ( page - 1 ) * size, size
        );
        if (messages == null || messages.isEmpty()) {
            return null;
        }

        List<SessionRecordVO> result = new ArrayList<>();

        SessionRecord msg = messages.get(0);
        User user1 = userMapper.getById(msg.getSenderId());
        User user2 = userMapper.getById(msg.getReceiverId());

        for (SessionRecord message : messages) {
            String senderName = (Objects.equals(message.getSenderId(), user1.getUserId())) ? user1.getRealName() : user2.getRealName();
            SessionRecordVO vo = SessionRecordVO.builder()
                    .sessionId(message.getSessionId())
                    .content(message.getContent())
                    .senderId(message.getSenderId())
                    .senderName(senderName)
                    .createdAt(message.getCreatedAt())
                    .build();
            result.add(vo);
        }

        return result;
    }

    private SessionRecord dtoToEntity(MessageDTO message) {
        return SessionRecord.builder()
                .sessionId(message.getRoomId())
                .senderId(message.getSenderId())
                .receiverId(message.getReceiverId())
                .content(message.getContent())
                .build();
    }
}
