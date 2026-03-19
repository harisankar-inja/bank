package com.harts.bank.model;

import com.harts.bank.api.request.dto.Account;
import lombok.Data;

import java.util.List;

@Data
public class CustomerDetails {
    private String customerId; //cif
    private String bankName;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String adhaarNumber;
    private String panNumber;
    private List<Account> accounts;
    private List<Account> loanAccounts;
}
