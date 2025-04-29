package com.ecnu.service.impl;

import com.ecnu.entity.RequestRecord;
import com.ecnu.entity.SessionRecord;
import com.ecnu.entity.User;
import com.ecnu.mapper.RequestRecordMapper;
import com.ecnu.mapper.UserMapper;
import com.ecnu.service.RequestRecordService;
import com.ecnu.vo.RequestRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class RequestRecordServiceImpl implements RequestRecordService {

    @Autowired
    private RequestRecordMapper requestRecordMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<RequestRecordVO> getHistoryMessages(Long requestId, Long page, Long size) {
        List<RequestRecord> records =  requestRecordMapper.selectByRequestId(requestId, 0L, 0L);

        if (records == null || records.isEmpty()) {
            return null;
        }

        List<RequestRecordVO> vos = new ArrayList<RequestRecordVO>();

        RequestRecord msg = records.get(0);
        User user1 = userMapper.getById(msg.getSenderId());
        User user2 = userMapper.getById(msg.getReceiverId());

        for (RequestRecord record : records) {
            String senderName = (Objects.equals(record.getSenderId(), user1.getUserId())) ? user1.getRealName() : user2.getRealName();
            RequestRecordVO vo = RequestRecordVO.builder()
                    .requestId(record.getRequestId())
                    .senderId(record.getSenderId())
                    .content(record.getContent())
                    .createdAt(record.getCreatedAt())
                    .senderName(senderName)
                    .build();
            vos.add(vo);
        }
        return vos;
    }
}
