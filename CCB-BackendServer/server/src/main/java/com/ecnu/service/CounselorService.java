package com.ecnu.service;

import com.ecnu.dto.CounselorHistoryDTO;
import com.ecnu.dto.SessionAddAdviceDTO;
import com.ecnu.entity.Counselor;
import com.ecnu.vo.*;

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
    List<String> getSchedule();

    /**
     * 获取和咨询师关联的督导ID
     * @return
     */
    List<Long> getSupervisorIds(Long counselorId);

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

    /**
     * 获取Id对应的咨询师的历史记录
     * @return
     */
    CounselorHistoryVO getHistory(CounselorHistoryDTO counselorHistoryDTO);

    /**
     * 获取Id对应的客户的当前会话信息
     * @param sessionid,clientid
     * @return
     */
    CounselorSessionVO getSession(Long sessionid, Long clientid);

    /**
     * 添加咨询评价
     * @param sessionAddAdviceDTO
     */
    void addSessionAdvice(SessionAddAdviceDTO sessionAddAdviceDTO);


    /**
     * 获取咨询师详情
     * @param counselorId
     */
    CounselorDetailVO getCounselorDetailById(Long counselorId);


    String getBio();

    void updateBio(String bio);


    /**
     * 咨询师向督导发起求助
     * @param supervisorId
     */
    Long addRequest(Long supervisorId);


    /**
     * 咨询师求助求助
     * @param requestId
     */
    void endRequest(Long requestId);

}
