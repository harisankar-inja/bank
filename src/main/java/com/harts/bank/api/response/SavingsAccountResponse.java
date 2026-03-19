package com.harts.bank.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.harts.bank.enums.AccountType;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SavingsAccountResponse {

    private String accountNumber;
    private String cif;
    private String accountHolderName;
    private String bankName;
    private String branchName;
    private String ifscCode;
    private AccountType accountType;
    private boolean active;
    private double balance;
}
