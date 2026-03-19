package com.harts.bank.api.request.dto;

import lombok.Data;

@Data
public class Account {

    private String accountNumber;
    private String accountType;
    private String loanType;
}
