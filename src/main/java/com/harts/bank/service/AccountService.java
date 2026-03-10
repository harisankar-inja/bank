package com.harts.bank.service;

import com.harts.bank.api.request.AccountRequest;
import com.harts.bank.api.response.Account;

import java.util.List;

public interface AccountService {

    Account createAccount(AccountRequest accountRequest, boolean isActive);

    List<Account> getAccountsByCustomerInfoFile(String cif);

    Account getAccountDetails(String accountNumber);
}
