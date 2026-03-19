package com.harts.bank.api.request.dto;

import lombok.Data;

import java.util.List;

@Data
public class BankDetails {

    private String cif;
    private String bankName;
    private List<Account> accounts;
}
