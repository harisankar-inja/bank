package com.harts.bank.enums;

public enum SubAccountType {

    GOLD_LOAN("Gold Loan"),
    PERSONAL_LOAN("Personal Loan"),
    HOME_LOAN("Home Loan"),
    VEHICLE_LOAN("Vehicle Loan"),
    EDUCATION_LOAN("Education Loan"),
    BUSINESS_LOAN("Business Loan");

    private final String displayName;

    SubAccountType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
