package com.harts.bank.service;

import java.util.List;

public interface AccountService<T, R> {
    R createAccount(T accountRequest);

    List<R> getAccountsByCustomerInfoFile(String cif);

    R getAccountDetails(String accountNumber);
}
