package com.ecnu.service.impl;

import com.ecnu.constant.SessionRecordConstant;
import com.ecnu.constant.SessionStatusConstant;
import com.ecnu.entity.Counselor;
import com.ecnu.entity.Session;
import com.ecnu.entity.User;
import com.ecnu.exception.*;
import com.ecnu.mapper.SessionsMapper;
import com.ecnu.mapper.UserMapper;
import com.ecnu.service.CounselorService;
import com.ecnu.service.SessionsService;
import com.ecnu.vo.ClientSessionExportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class SessionsServiceImpl implements SessionsService {
    @Autowired
    private SessionsMapper sessionMapper;

    @Autowired
    private CounselorService counselorService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private SessionsMapper sessionsMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 验证会话访问权限（核心方法）
     */
    public Session validateSessionAccess(Long sessionId, Long userId) {
        Session session = sessionMapper.getById(sessionId);
        if (session == null) {
            throw new SessionNotFoundException("会话不存在");
        }

        if (!isParticipant(session, userId)) {
            throw new UnauthorizedAccessException("无权限访问此会话");
        }

        return session;
    }
    /**
     * 创建新会话（带并发控制）
     */
    @Transactional
    public Session startSession(Long clientId, Long counselorId) {
        Session existSession = sessionMapper.getByParticipantIds(clientId, counselorId);

        if (existSession != null) {
            return existSession;
        }

        Counselor counselor = counselorService.getById(counselorId);

        Session session = Session.builder()
                .clientId(clientId)
                .counselorId(counselorId)
                .startTime(LocalDateTime.now())
                .build();

        if (counselor.getCurrentSessions() >= counselor.getMaxSessions()) {
            session.setStatus(SessionStatusConstant.PENDING);
        } else {
            session.setStatus(SessionStatusConstant.ACTIVE);
            counselorService.incrementCurrentSessions(counselorId);
        }

        sessionMapper.insertSession(session);
        return session;
    }
    /**
     * 结束会话
     */
    @Transactional
    public void endSession(Long sessionId, Integer rating) {
        Session session = sessionsMapper.getById(sessionId);

        if (session == null) {
            throw new IllegalSessionOperationException("非法SessionID");
        }

        if (SessionStatusConstant.CLOSED.equals(session.getStatus())) {
            throw new IllegalSessionOperationException("会话已结束");
        }
        session.setStatus(SessionStatusConstant.CLOSED);
        session.setRating(rating);
        session.setEndTime(LocalDateTime.now());
        sessionMapper.updateSessionStatus(session);

        Long counselorId = session.getCounselorId();
        Counselor counselor = counselorService.getById(counselorId);


        if (Objects.equals(counselor.getCurrentSessions(), counselor.getMaxSessions())) {
            Session earliestSession = sessionMapper.getEarliestPendingSession(counselorId);
            if (earliestSession == null) {
                counselorService.decrementCurrentSessions(counselorId);
                return;
            }
            earliestSession.setStatus(SessionStatusConstant.ACTIVE);
            sessionMapper.updateSessionStatus(session);
        } else {
            counselorService.decrementCurrentSessions(counselorId);
        }
    }

    public List<Long> getRelatedSession(Long userId) {
        List<Session> sessions = sessionMapper.getByParticipantId(userId);
        return sessions.stream()
                .filter(Objects::nonNull)
                .map(Session::getSessionId)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClientSessionExportVO> getAllSession(Long userId) {
        List<Session> sessions = sessionMapper.getByClientId(userId);
        List<ClientSessionExportVO> result = new ArrayList<>();
        for (Session session : sessions) {
            Long counselorId = session.getCounselorId();
            User counselor = userMapper.getById(counselorId);
            ClientSessionExportVO vo = ClientSessionExportVO.builder()
                    .counselorName(counselor.getRealName())
                    .sessionId(session.getSessionId())
                    .startTime(session.getStartTime())
                    .build();
            result.add(vo);
        }

        return result;
    }

    private boolean isParticipant(Session session, Long userId) {
        return userId.equals(session.getClientId()) || userId.equals(session.getCounselorId());
    }
}
