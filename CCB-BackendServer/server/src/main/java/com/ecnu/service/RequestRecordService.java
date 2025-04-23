package com.ecnu.service;

import com.ecnu.vo.RequestRecordVO;

import java.util.List;

public interface RequestRecordService {

    /**
     * 获取求助历史记录
     * @return List<RequestRecordVO>
     */
    List<RequestRecordVO> getHistoryMessages(Long requestId, Long page, Long size);
}
