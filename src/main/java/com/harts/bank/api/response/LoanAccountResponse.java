package com.harts.bank.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.harts.bank.enums.AccountType;
import com.harts.bank.enums.LoanType;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanAccountResponse {


    // common fields for both savings and loan accounts
    private String accountNumber;
    private String cif;
    private String accountHolderName;
    private String bankName;
    private String branchName;
    private String ifscCode;
    private AccountType accountType;
    private LoanType loanType;
    private double loanAmount;
    private boolean active;

    private int loanTermInYears;
    private double interestRate;

}
