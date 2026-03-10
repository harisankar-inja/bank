package com.harts.bank.service.impl;

import com.harts.bank.api.request.SavingsAccountRequest;
import com.harts.bank.api.response.SavingsAccountResponse;
import com.harts.bank.model.Customer;
import com.harts.bank.model.SavingsAccount;
import com.harts.bank.repository.AccountRepo;
import com.harts.bank.repository.CustomerRepo;
import com.harts.bank.service.AccountService;
import com.harts.bank.service.CustomerService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

//@Service
@RequiredArgsConstructor
public class LoanAccountService implements AccountService {

    private final CustomerRepo customerRepo;
    private final CustomerService customerService;
    private final AccountRepo accountRepo;
    private final SavingsAccountService savingsAccountService;

    /***
     * Loan account creation is a two step process, first we need to create a savings account for the customer (if not already exists)
     * and then we can create a loan account for the customer. This is because we need to link the loan account to the savings account for the customer.
     * @param accountRequest
     * @param checkForExistingCustomer - this flag is set to false only if the customer does not exist, so that we can skip db call to fetch customer details again
     *                 in savings account creation process.
     * @return
     */
    @Override
    public SavingsAccountResponse createAccount(SavingsAccountRequest accountRequest, boolean checkForExistingCustomer) {
        Optional<Customer> customer = customerRepo.findByAdhaarNumWithBank(accountRequest.getAadharNumber(), accountRequest.getBankName());
        SavingsAccountResponse account = new SavingsAccountResponse();
        if(customer.isEmpty()){
            account = savingsAccountService.createAccount(accountRequest, false);
        } else {
            Optional<SavingsAccount> acc = accountRepo.findByCustomerIdAndBank(customer.get().getCustomerId(), accountRequest.getBankName());
        }

        return account;
    }

    @Override
    public List<SavingsAccountResponse> getAccountsByCustomerInfoFile(String cif) {
        return List.of();
    }

    @Override
    public SavingsAccountResponse getAccountDetails(String accountNumber) {
        return null;
    }
}
