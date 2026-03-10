package com.harts.bank.model;

import com.harts.bank.enums.AccountType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SavingsAccount {


    private int accountId;
    private String accountNumber;
    private String cif;
    private String accountHolderName;
    private String bankName;
    private String branchName;
    private String ifscCode;
    private AccountType accountType;
    private double balance;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
