package com.ecnu.service;

import com.ecnu.entity.Counselor;
import com.ecnu.vo.CounselorInfo;

public interface CounselorService {
    /**
     * 获取咨询师信息
     * @return
     */
    CounselorInfo getCounselorInfo();
    /**
     * 给Id对应的咨询师的当前会话数加一
     * @return
     */
    void incrementCurrentSessions(Long counselorId);

    /**
     * 给Id对应的咨询师的当前会话数减一
     * @return
     */
    void decrementCurrentSessions(Long counselorId);

    /**
     * 获取Id对应的咨询师
     * @return
     */
    Counselor getById(Long counselorId);
}
