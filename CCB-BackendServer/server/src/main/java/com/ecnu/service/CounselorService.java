package com.ecnu.service;

import com.ecnu.vo.CounselorInfo;
import com.ecnu.vo.RecentSession;
import com.ecnu.vo.Session;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CounselorService {
    /**
     * 获取咨询师信息
     * @return
     */
    CounselorInfo getCounselorInfo();

    /**
     * 获取咨询师排班信息
     * @return
     */
    List<LocalDate> getSchedule();

    /**
     * 获取咨询师最近咨询信息
     * @return
     */
    List<RecentSession> getRecentSessions();

    /**
     * 获取咨询师当前咨询列表
     * @return
     */
    List<Session> getSessionList();
}
