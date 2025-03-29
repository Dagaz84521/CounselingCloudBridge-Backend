package com.ecnu.service.impl;

import com.ecnu.entity.Counselor;
import com.ecnu.entity.Session;
import com.ecnu.entity.User;
import com.ecnu.exception.*;
import com.ecnu.mapper.SessionsMapper;
import com.ecnu.service.CounselorService;
import com.ecnu.service.SessionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class SessionsServiceImpl implements SessionsService {
    @Autowired
    private SessionsMapper sessionMapper;

    @Autowired
    private CounselorService counselorService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
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
    public Session createSession(Long clientId, Long counselorId) {
        // 使用Redis分布式锁
        String lockKey = "counselor_lock:" + counselorId;
        Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", 30, TimeUnit.SECONDS);

        try {
            if (Boolean.TRUE.equals(locked)) {
                Counselor counselor = counselorService.getById(counselorId);
                if (counselor.getCurrentSessions() >= counselor.getMaxSessions()) {
                    throw new CounselorBusyException("咨询师会话已满");
                }
                Session session = Session.builder()
                        .clientId(clientId)
                        .counselorId(counselorId)
                        .status("pending")
                        .startTime(LocalDateTime.now()) // 默认5分钟后开始
                        .build();

                sessionMapper.insertSession(session);
                counselorService.incrementCurrentSessions(counselorId);
                return session;
            }
            throw new ConcurrentSessionException("系统繁忙，请稍后重试");
        } finally {
            redisTemplate.delete(lockKey);
        }
    }
    /**
     * 结束会话
     */
    @Transactional
    public void endSession(Long sessionId, User operator) {
        Session session = validateSessionAccess(sessionId, operator.getUserId());

        if ("closed".equals(session.getStatus())) {
            throw new IllegalSessionOperationException("会话已结束");
        }
        session.setStatus("closed");
        session.setEndTime(LocalDateTime.now());
        sessionMapper.updateSessionStatus(session);

        counselorService.decrementCurrentSessions(session.getCounselorId());
    }

    private boolean isParticipant(Session session, Long userId) {
        return userId.equals(session.getClientId()) || userId.equals(session.getCounselorId());
    }
}
