package com.harts.bank.service.impl;

import com.harts.bank.api.request.LoanAccountRequest;
import com.harts.bank.api.response.LoanAccountResponse;
import com.harts.bank.exceptions.CustomerNotFoundException;
import com.harts.bank.model.Customer;
import com.harts.bank.model.SavingsAccount;
import com.harts.bank.repository.SavingsAccountRepo;
import com.harts.bank.repository.CustomerRepo;
import com.harts.bank.service.AccountService;
import com.harts.bank.service.CustomerService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

//@Service
@RequiredArgsConstructor
public class LoanAccountService implements AccountService<LoanAccountRequest, LoanAccountResponse> {

    private final CustomerRepo customerRepo;
    private final CustomerService customerService;
    private final SavingsAccountRepo accountRepo;
    private final SavingsAccountService savingsAccountService;

    /***
     * Loan account creation is a two step process, first we need to create a savings account for the customer (if not already exists)
     * and then we can create a loan account for the customer. This is because we need to link the loan account to the savings account for the customer.
     * @param accountRequest
     * @return
     */
    @Override
    public LoanAccountResponse createAccount(LoanAccountRequest accountRequest) {
        Optional<Customer> customer = customerRepo.findByAdhaarNumWithBank(accountRequest.getAadharNumber(), accountRequest.getBankName());
        LoanAccountResponse account = new LoanAccountResponse();
        if(customer.isEmpty()){
            throw new CustomerNotFoundException("Customer with Aadhar number " + accountRequest.getAadharNumber() + " not found in bank " + accountRequest.getBankName() + ". Cannot create loan account.");
        } else {
            Optional<SavingsAccount> acc = accountRepo.findByCustomerIdAndBank(customer.get().getCustomerId(), accountRequest.getBankName());
        }

        return account;
    }

    @Override
    public List<LoanAccountResponse> getAccountsByCustomerInfoFile(String cif) {
        return List.of();
    }

    @Override
    public LoanAccountResponse getAccountDetails(String accountNumber) {
        return null;
    }
}
