package com.ecnu.service.impl;

import com.ecnu.entity.RequestRecord;
import com.ecnu.mapper.RequestRecordMapper;
import com.ecnu.service.RequestRecordService;
import com.ecnu.vo.RequestRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RequestRecordServiceImpl implements RequestRecordService {

    @Autowired
    private RequestRecordMapper requestRecordMapper;

    @Override
    public List<RequestRecordVO> getHistoryMessages(Long requestId, Long page, Long size) {
        List<RequestRecord> records =  requestRecordMapper.selectByRequestId(requestId, 0L, 0L);
        List<RequestRecordVO> vos = new ArrayList<RequestRecordVO>();
        for (RequestRecord record : records) {
            RequestRecordVO vo = RequestRecordVO.builder()
                    .requestId(record.getRequestId())
                    .content(record.getContent())
                    .createdAt(record.getCreatedAt())
                    .senderId(record.getSenderId())
                    .build();
            vos.add(vo);
        }
        return vos;
    }
}
