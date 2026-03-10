package com.harts.bank.exceptions;

public class InActiveAccountException extends RuntimeException {
    public InActiveAccountException(String message) {
        super(message);
    }
}
