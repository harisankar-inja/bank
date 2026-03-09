package com.harts.bank.enums;

public enum AccountType {

    CHECKING("Checking"),
    SAVINGS("Savings"),
    CURRENT("Current"),
    SALARY("Salary"),
    CREDIT("Credit"),
    LOAN("Loan");

    private final String displayName;

    AccountType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
