package com.ecnu.service.impl;

import com.ecnu.constant.SessionStatusConstant;
import com.ecnu.context.BaseContext;
import com.ecnu.dto.CounselorHistoryDTO;
import com.ecnu.dto.CounselorTodaySessionDTO;
import com.ecnu.dto.SessionAddAdviceDTO;
import com.ecnu.entity.Counselor;
import com.ecnu.entity.Session;
import com.ecnu.entity.User;
import com.ecnu.mapper.CounselorMapper;
import com.ecnu.mapper.ScheduleMapper;
import com.ecnu.mapper.SessionsMapper;
import com.ecnu.mapper.UserMapper;
import com.ecnu.service.CounselorService;
import com.ecnu.vo.CounselorInfo;
import com.ecnu.vo.CounselorSessionVO;
import com.ecnu.vo.RecentSession;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
    private ScheduleMapper scheduleMapper;

    /**
     * 获取咨询师信息
     * @return
     */
    public CounselorInfo getCounselorInfo() {
        User user = userMapper.geById(BaseContext.getCurrentId());
        Counselor counselor = counselorMapper.getById(BaseContext.getCurrentId());
        CounselorTodaySessionDTO counselorTodaySessionDTO = sessionsMapper.getCounselorTodaySession(BaseContext.getCurrentId(), SessionStatusConstant.CLOSED);
        CounselorInfo counselorInfo = new CounselorInfo().builder()
                .realName(user.getRealName())
                .avatarUrl(user.getAvatarUrl())
                .totalSessions(sessionsMapper.getTotalSessions(BaseContext.getCurrentId(), SessionStatusConstant.CLOSED))
                .todaySessions(counselorTodaySessionDTO.getTodaySessions())
                .todayHours(counselorTodaySessionDTO.getTodayHours())
                .currentSessions(counselor.getCurrentSessions())
                .build();
        return counselorInfo;
    }

    /**
     * 获取咨询师的排班信息
     * @return
     */
    public List<LocalDate> getSchedule() {
        List<LocalDate> schedule = scheduleMapper.getSchedule(BaseContext.getCurrentId());
        return schedule;
    }

    /**
     * 获取咨询师最近的会话
     * @return
     */
    public List<RecentSession> getRecentSessions() {
        List<RecentSession> recentSessions = sessionsMapper.getRecentSessions(BaseContext.getCurrentId(), SessionStatusConstant.CLOSED);
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

    @Override
    public void incrementCurrentSessions(Long counselorId) {
        counselorMapper.updateCurrentSessions(counselorId, 1);
    }

    @Override
    public void decrementCurrentSessions(Long counselorId) {
        counselorMapper.updateCurrentSessions(counselorId, 1);
    }

    @Override
    public Counselor getById(Long counselorId) {
        return counselorMapper.getById(counselorId);
    }

    /**
     * 获取咨询师的历史会话
     * @return
     */
    public List<RecentSession> getHistory(CounselorHistoryDTO counselorHistoryDTO) {
        PageHelper.startPage(counselorHistoryDTO.getPage(), counselorHistoryDTO.getPagesize());
        counselorHistoryDTO.setCounselorId(BaseContext.getCurrentId());
        Page<RecentSession> page = sessionsMapper.getHistory(counselorHistoryDTO);
        return page.getResult();
    }

    /**
     * 获取咨询师的咨询页面信息
     * @param clientid
     * @return
     */
    public CounselorSessionVO getSession(Long sessionid, Long clientid) {
        User user = userMapper.geById(clientid);
        Session session = sessionsMapper.getById(sessionid);
        CounselorSessionVO counselorSessionVO = new CounselorSessionVO().builder()
                .realName(user.getRealName())
                .phoneNumber(user.getPhoneNumber())
                .avatarUrl(user.getAvatarUrl())
                .startTime(session.getStartTime())
                .rating(session.getRating())
                .build();
        return counselorSessionVO;
    }

    /**
     * 咨询师添加咨询评价
     * @param sessionAddAdviceDTO
     */
    public void addSessionAdvice(SessionAddAdviceDTO sessionAddAdviceDTO, Long sessionid) {
        sessionsMapper.addSessionAdvice(sessionAddAdviceDTO, sessionid);
    }
}
