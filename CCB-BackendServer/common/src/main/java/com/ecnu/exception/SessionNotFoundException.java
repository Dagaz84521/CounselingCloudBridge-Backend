package com.ecnu.exception;

/**
 * 无 SessionID 对应的合法会话异常
 */
public class SessionNotFoundException extends BaseException {
    public SessionNotFoundException(String msg) { super(msg); }
}
