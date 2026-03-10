package com.harts.bank.api.response;

import com.harts.bank.enums.AccountType;
import com.harts.bank.enums.SubAccountType;

public class LoanAccountResponse {


    // common fields for both savings and loan accounts
    private String accountNumber;
    private String cif;
    private String accountHolderName;
    private String bankName;
    private String branchName;
    private String ifscCode;
    private AccountType accountType;
    private boolean active;

    // fields specific to savings account
    private double balance;

    // fields specific to loan account
    private SubAccountType subAccountType;
    private double loanAmount;
}
