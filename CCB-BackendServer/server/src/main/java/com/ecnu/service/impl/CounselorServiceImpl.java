package com.ecnu.service.impl;

import com.ecnu.constant.SessionStatusConstant;
import com.ecnu.context.BaseContext;
import com.ecnu.dto.CounselorTodaySessionVO;
import com.ecnu.entity.Counselor;
import com.ecnu.entity.Session;
import com.ecnu.entity.User;
import com.ecnu.mapper.CounselorMapper;
import com.ecnu.mapper.CounselorScheduleMapper;
import com.ecnu.mapper.SessionsMapper;
import com.ecnu.mapper.UserMapper;
import com.ecnu.service.CounselorService;
import com.ecnu.vo.CounselorInfo;
import com.ecnu.vo.RecentSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CounselorServiceImpl implements CounselorService {

    @Autowired
    private CounselorMapper counselorMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SessionsMapper sessionsMapper;
    @Autowired
    private CounselorScheduleMapper counselorScheduleMapper;

    /**
     * 获取咨询师信息
     * @return
     */
    public CounselorInfo getCounselorInfo() {
        User user = userMapper.geById(BaseContext.getCurrentId());
        Counselor counselor = counselorMapper.getById(BaseContext.getCurrentId());
        CounselorTodaySessionVO counselorTodaySessionVO = sessionsMapper.getCounselorTodaySession(BaseContext.getCurrentId(), SessionStatusConstant.CLOSED);
        CounselorInfo counselorInfo = new CounselorInfo().builder()
                .realName(user.getRealName())
                .avatarUrl(user.getAvatarUrl())
                .totalSessions(sessionsMapper.getTotalSessions(BaseContext.getCurrentId(), SessionStatusConstant.CLOSED))
                .todaySessions(counselorTodaySessionVO.getTodaySessions())
                .todayHours(counselorTodaySessionVO.getTodayHours())
                .currentSessions(counselor.getCurrentSessions())
                .build();
        return counselorInfo;
    }

    /**
     * 获取咨询师的排班信息
     * @return
     */
    public List<LocalDate> getSchedule() {
        List<LocalDate> schedule = counselorScheduleMapper.getSchedule(BaseContext.getCurrentId());
        return schedule;
    }

    /**
     * 获取咨询师最近的会话
     * @return
     */
    public List<RecentSession> getRecentSessions() {
        List<Session> sessions = sessionsMapper.getRecentSessions(BaseContext.getCurrentId(), SessionStatusConstant.CLOSED);
        List<RecentSession> recentSessions = new ArrayList<>();
        for (Session session : sessions) {
            RecentSession recentSession = new RecentSession();
            BeanUtils.copyProperties(session, recentSession);
            User user = userMapper.geById(session.getClientId());
            recentSession.setRealName(user.getRealName());
            recentSessions.add(recentSession);
        }
        return recentSessions;
    }

    /**
     * 获取咨询师的咨询列表
     *
     * @return
     */
    public List<com.ecnu.vo.Session> getSessionList() {
        List<Session> sessions = sessionsMapper.getSessionList(BaseContext.getCurrentId(), SessionStatusConstant.ACTIVE);
        List<com.ecnu.vo.Session> sessionList = new ArrayList<>();
        for (Session session : sessions) {
            com.ecnu.vo.Session sessionVO = new com.ecnu.vo.Session();
            BeanUtils.copyProperties(session, sessionVO);
            User user = userMapper.geById(session.getClientId());
            sessionVO.setRealName(user.getRealName());
            sessionList.add(sessionVO);
        }
        return sessionList;
    }
}
