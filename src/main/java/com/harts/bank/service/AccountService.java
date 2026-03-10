package com.harts.bank.service;

import com.harts.bank.api.request.SavingsAccountRequest;
import com.harts.bank.api.response.SavingsAccountResponse;

import java.util.List;

public interface AccountService {

    SavingsAccountResponse createAccount(SavingsAccountRequest accountRequest, boolean isActive);

    List<SavingsAccountResponse> getAccountsByCustomerInfoFile(String cif);

    SavingsAccountResponse getAccountDetails(String accountNumber);
}
