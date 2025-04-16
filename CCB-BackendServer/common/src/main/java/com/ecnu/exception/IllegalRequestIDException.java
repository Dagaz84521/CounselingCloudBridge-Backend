package com.ecnu.exception;
/**
 * 非法requestID, 无对应求助
 */
public class IllegalRequestIDException extends BaseException {
    public IllegalRequestIDException(String message) {
        super(message);
    }
}
