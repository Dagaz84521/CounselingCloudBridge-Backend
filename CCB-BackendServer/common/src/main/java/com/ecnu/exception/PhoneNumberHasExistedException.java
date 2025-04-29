package com.ecnu.exception;
/**
 * 已存在重复手机号错误
 */
public class PhoneNumberHasExistedException extends BaseException {
    public PhoneNumberHasExistedException(String message) {
        super(message);
    }
}
