package com.ecnu.service;

import com.ecnu.dto.MessageDTO;
import com.ecnu.vo.SessionRecordVO;

import java.util.List;

public interface SessionRecordService {

    /**
     * 获取会话历史记录
     * @return List<SessionRecordVO>
     */
    List<SessionRecordVO> getHistoryMessages(Long sessionId, Long page, Long size);
}
