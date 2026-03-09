package com.harts.bank.service;

import com.harts.bank.model.Account;

import java.util.List;

public interface AccountService {

    Account createAccount(String accountHolderName, double initialDeposit);

    List<Account> getAccountsByCustomerInfoFile(String cif);

    Account getAccountDetails(String accountNumber);
}
