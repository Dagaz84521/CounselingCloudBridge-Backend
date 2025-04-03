package com.ecnu.exception;

public class AccountHasExistedException extends RuntimeException {
    public AccountHasExistedException(String message) {
        super(message);
    }
}
