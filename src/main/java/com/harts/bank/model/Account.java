package com.harts.bank.model;

import com.harts.bank.enums.AccountType;
import com.harts.bank.enums.SubAccountType;
import lombok.Data;

@Data
public class Account {

    private String accountNumber;
    private String accountHolderName;
    private String bankName;
    private String branchName;
    private String ifscCode;
    private AccountType accountType;
    private SubAccountType subAccountType;
}
