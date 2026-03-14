package com.harts.bank.enums;

public enum EmploymentType {

    SALARIED("Salaried"),
    SELF_EMPLOYED("Self-Employed"),
    UNEMPLOYED("Unemployed");

    private final String displayName;

    EmploymentType(String displayName) {
        this.displayName = displayName;
    }
}
