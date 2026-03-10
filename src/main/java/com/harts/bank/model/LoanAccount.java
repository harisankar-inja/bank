package com.harts.bank.model;

import com.harts.bank.enums.AccountType;
import com.harts.bank.enums.SubAccountType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoanAccount {

    private String accountHolderName;
    private String cif; // cif
    private String loanAccountNumber;
    private String linkedSavingsAccountNumber; // for EMI deductions
    private AccountType accountType; // should be LOAN
    private SubAccountType subAccountType; // HOME_LOAN, PERSONAL_LOAN, AUTO_LOAN, EDUCATION_LOAN
    private double loanAmount;
    private int loanTermInYears;
    private double interestRate;
    private int creditScore;
    private int annualIncome;
    private int existingEmis;
    private String employmentType; // salaried, self-employed, unemployed
    private boolean active; // to track if the loan account is active or closed
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy; // userId of the employee who created the account
    private String updatedBy; // userId of the employee who last updated the account
}
