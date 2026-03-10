package com.harts.bank.exceptions;

public class InActiveCifException extends RuntimeException {
    public InActiveCifException(String message) {
        super(message);
    }
}
