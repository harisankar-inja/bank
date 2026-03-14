package com.harts.bank.exceptions;

public class LoanNotEligibleException extends RuntimeException {
    public LoanNotEligibleException(String message) {
        super(message);
    }
}
