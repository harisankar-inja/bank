package com.harts.bank.api.request;

import com.harts.bank.enums.AccountType;
import com.harts.bank.enums.EmploymentType;
import com.harts.bank.enums.LoanType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
public class LoanAccountRequest {

    @NonNull
    private AccountType accountType;
    @NotBlank
    private String aadharNumber;
    @NotBlank
    private String panNumber;
    @NotBlank
    private String bankName;
    @NotBlank
    private String bankBranch;
    @NotBlank
    private String ifscCode;
    @NonNull
    private LoanType subAccountType;
    private boolean requestedLoanAmountAutoCal; // if true, use loanAmountRequested, else auto-calculate based on credit score and income
    private double loanAmountRequested;
    private boolean interestRateAndTermAutoCal; // for auto-calculation based on credit score and income, if false, use provided values
    private int loanTermInYears;    //to override auto-calculated term if needed
    private double interestRate;    //to override auto-calculated interest rate if needed
    private int creditScore;
    private double monthlyIncome;
    private double annualIncome;
    @NonNull
    private EmploymentType employmentType; // salaried, self-employed, unemployed
    private String createdBy;
    private String updatedBy;
}
