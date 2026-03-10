package com.harts.bank.api.request;

import com.harts.bank.enums.AccountType;
import com.harts.bank.enums.SubAccountType;
import com.harts.bank.model.Address;

public class LoanAccountRequest {

    // for any type of account creation, these details are mandatory
    private AccountType accountType;
    private String aadharNumber;
    private String panNumber;
    private String bankName;
    private String bankBranch;
    private String ifscCode;

    // for customers opening savings account
    private double initialDeposit;

    // for customers opening loan account
    private SubAccountType subAccountType;
    private double loanAmount;
    private int loanTermInYears;
    private double interestRate;
    private int creditScore;
    private int annualIncome;
    private int existingEmis;
    private String employmentType; // salaried, self-employed, unemployed

    // customer details for new customers
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String adhaarNumber;
    private Address address;
}
