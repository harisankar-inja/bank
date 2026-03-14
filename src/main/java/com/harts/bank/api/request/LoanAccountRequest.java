package com.harts.bank.api.request;

import com.harts.bank.enums.AccountType;
import com.harts.bank.enums.EmploymentType;
import com.harts.bank.enums.SubAccountType;
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
    private SubAccountType subAccountType;
    private double loanAmountRequested;
    private int loanTermInYears;
    private double interestRate;
    private int creditScore;
    private double monthlyIncome;
    private double annualIncome;
    private int existingEmis;
    @NonNull
    private EmploymentType employmentType; // salaried, self-employed, unemployed
    private String createdBy;
    private String updatedBy;
}
