package com.ecnu.service;

import com.ecnu.entity.Session;
import com.ecnu.entity.User;

public interface SessionsService {

    /**
     * 验证
     * @return sessionId对应的合法的会话
     */
    Session validateSessionAccess(Long sessionId, Long userId);
    /**
     * 创建新会话（带并发控制）
     * @return 新创建好的会话
     */
    Session createSession(Long clientId, Long counselorId);
    /**
     * 结束会话
     */
    void endSession(Long sessionId, User operator);

}