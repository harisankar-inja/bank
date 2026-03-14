package com.harts.bank.service;

import com.harts.bank.api.request.LoanAccountRequest;
import com.harts.bank.model.Customer;
import com.harts.bank.model.LoanAccount;
import com.harts.bank.model.SavingsAccount;
import com.harts.bank.service.impl.LoanEligibilityResponse;

import java.util.List;

public interface LoanEligibilityService {
    LoanEligibilityResponse validateLoanAccountRequest(LoanAccountRequest request, Customer customer, SavingsAccount savingsAccount, List<LoanAccount> loanAccounts);
}

