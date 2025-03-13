package com.ecnu.exception;

/**
 * 账号被禁用异常
 */
public class AccountBannedException extends BaseException {

    public AccountBannedException(String msg) {
        super(msg);
    }

}
