package com.ecnu.service;

import com.ecnu.dto.SessionRecordDTO;
import com.ecnu.vo.SessionRecordVO;

import java.util.List;

public interface ChatService {
    /**
     * 给对应的Session插入会话记录，并推送信息
     * @return SessionRecordVO
     */
    SessionRecordVO sendMessage(SessionRecordDTO dto);

    /**
     * 获取会话历史记录
     * @return List<SessionRecordVO>
     */
    List<SessionRecordVO> getHistoryMessages(Long sessionId, int page, int size);
}
