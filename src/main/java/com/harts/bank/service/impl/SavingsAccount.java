package com.harts.bank.service.impl;

import com.harts.bank.model.Account;
import com.harts.bank.service.AccountService;

import java.util.List;

public class SavingsAccount implements AccountService {

    @Override
    public Account createAccount(String accountHolderName, double initialDeposit) {
        return null;
    }

    @Override
    public List<Account> getAccountsByCustomerInfoFile(String cif) {
        return List.of();
    }

    @Override
    public Account getAccountDetails(String accountNumber) {
        return null;
    }
}
