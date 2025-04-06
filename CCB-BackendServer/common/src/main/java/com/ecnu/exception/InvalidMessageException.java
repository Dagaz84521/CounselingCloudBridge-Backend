package com.ecnu.exception;

/**
 * 非法消息
 */
public class InvalidMessageException extends BaseException {
    public InvalidMessageException(String msg) {
        super(msg);
    }
}
