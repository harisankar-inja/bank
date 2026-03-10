package com.harts.bank.service.impl;

import com.harts.bank.api.request.SavingsAccountRequest;
import com.harts.bank.enums.AccountType;
import com.harts.bank.exceptions.CustomerNotFoundException;
import com.harts.bank.api.response.SavingsAccountResponse;
import com.harts.bank.exceptions.InvalidRequestException;
import com.harts.bank.model.Customer;
import com.harts.bank.model.SavingsAccount;
import com.harts.bank.repository.SavingsAccountRepo;
import com.harts.bank.repository.CustomerRepo;
import com.harts.bank.service.AccountService;
import com.harts.bank.service.CustomerService;
import com.harts.bank.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.harts.bank.enums.AccountType.LOAN;

@Service
@RequiredArgsConstructor
public class SavingsAccountService implements AccountService {

    private final CustomerRepo customerRepo;
    private final CustomerService customerService;
    private final SavingsAccountRepo accountRepo;

    @Override
    @Transactional
    public SavingsAccountResponse createAccount(SavingsAccountRequest accountRequest, boolean checkForExistingCustomer) {
        Optional<Customer> customer = Optional.empty();
        SavingsAccount savingsAccount = new SavingsAccount();
        if(checkForExistingCustomer) {
            customer = customerRepo.findByAdhaarNumWithBank(accountRequest.getAadharNumber(), accountRequest.getBankName());
        }
        if (customer.isPresent()) {
            if(customer.get().isActive()) {
                if (checkIfSavingsAccountExists(customer.get().getCustomerId(), accountRequest.getBankName(), accountRequest.getAccountType())) {
                    throw new CustomerNotFoundException("Customer with Aadhar number " + accountRequest.getAadharNumber() + " already has a savings account with bank " + accountRequest.getBankName());
                }
                setSavingsAccountDetails(savingsAccount, accountRequest, customer.get().getCustomerId());
                savingsAccount.setAccountHolderName(customer.get().getFirstName() + " " + customer.get().getLastName());
            } else {
                throw new CustomerNotFoundException("Cif is already present! Customer with Aadhar number " + accountRequest.getAadharNumber()
                        + " is not active. Cannot create savings account.");
            }
        } else {
            if(validateAccountRequest(accountRequest)) {
                throw new InvalidRequestException("Invalid account request. firstName, lastName, phoneNumber, email, " +
                        "address are mandatory fields and must be valid.");
            }
            Customer newCustomer = buildCustomerDetails(accountRequest);
            customerService.registerCustomer(newCustomer, false);
            setSavingsAccountDetails(savingsAccount, accountRequest, newCustomer.getCustomerId());
            savingsAccount.setAccountHolderName(accountRequest.getFirstName() + " " + accountRequest.getLastName());
        }
        accountRepo.persist(savingsAccount);
        SavingsAccountResponse account = new SavingsAccountResponse();
        mapSavingsAccountToAccount(account, savingsAccount);
        return account;
    }

    @Override
    public List<SavingsAccountResponse> getAccountsByCustomerInfoFile(String cif) {
        List<SavingsAccount> savingsAccounts = accountRepo.findByCustomerId(cif, LOAN);
        if (savingsAccounts.isEmpty()) {
            throw new CustomerNotFoundException("No accounts found for customer with CIF " + cif);
        }
        List<SavingsAccountResponse> accounts = new ArrayList<>();
        for(SavingsAccount savingsAccount : savingsAccounts) {
            if (savingsAccount.getAccountType() != LOAN) {
                SavingsAccountResponse account = new SavingsAccountResponse();
                mapSavingsAccountToAccount(account, savingsAccount);
                accounts.add(account);
            }
        }
        return accounts;
    }

    @Override
    public SavingsAccountResponse getAccountDetails(String accountNumber) {
        Optional<SavingsAccount> optionalAccount = accountRepo.findByAccountNumber(accountNumber);
        if (optionalAccount.isEmpty()) {
            throw new CustomerNotFoundException("Account with number " + accountNumber + " not found");
        }
        SavingsAccount savingsAccount = optionalAccount.get();
        if (savingsAccount.getAccountType() == LOAN) {
            throw new CustomerNotFoundException("Account with number " + accountNumber + " is a LOAN account and cannot be retrieved");
        }
        SavingsAccountResponse account = new SavingsAccountResponse();
        mapSavingsAccountToAccount(account, savingsAccount);
        return account;
    }

    private boolean validateAccountRequest(SavingsAccountRequest accountRequest) {
        return isBlank(accountRequest.getFirstName())
            || isBlank(accountRequest.getLastName())
            || isBlank(accountRequest.getPhoneNumber())
            || isBlank(accountRequest.getEmail())
            || accountRequest.getAddress() == null;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean checkIfSavingsAccountExists(String customerId, String bankName, AccountType accountType) {
        Optional<SavingsAccount> optionalAccount = accountRepo.findByCustomerIdAndBankAndAccountType(customerId, bankName, accountType);
        return optionalAccount.isPresent();
    }

    private void setSavingsAccountDetails(SavingsAccount savingsAccount, SavingsAccountRequest accountRequest, String customerId) {
        savingsAccount.setCif(customerId);
        savingsAccount.setAccountNumber(CommonUtils.generateRandomNumber(12));
        savingsAccount.setBankName(accountRequest.getBankName());
        savingsAccount.setBranchName(accountRequest.getBankBranch());
        savingsAccount.setIfscCode(accountRequest.getIfscCode());
        savingsAccount.setAccountType(accountRequest.getAccountType());
        savingsAccount.setBalance(accountRequest.getInitialDeposit());
    }

    private Customer buildCustomerDetails(SavingsAccountRequest accountRequest) {
        Customer customer = new Customer();
        customer.setCustomerId(CommonUtils.generateRandomNumber(8));
        customer.setBankName(accountRequest.getBankName());
        customer.setFirstName(accountRequest.getFirstName());
        customer.setLastName(accountRequest.getLastName());
        customer.setAdhaarNumber(accountRequest.getAadharNumber());
        customer.setPanNumber(accountRequest.getPanNumber());
        customer.setPhoneNumber(accountRequest.getPhoneNumber());
        customer.setEmail(accountRequest.getEmail());
        customer.setActive(true);
        customer.setAddress(accountRequest.getAddress());
        return customer;
    }

    private void mapSavingsAccountToAccount(SavingsAccountResponse account, SavingsAccount savingsAccount) {
        account.setAccountNumber(savingsAccount.getAccountNumber());
        account.setCif(savingsAccount.getCif());
        account.setAccountHolderName(savingsAccount.getAccountHolderName());
        account.setBankName(savingsAccount.getBankName());
        account.setBranchName(savingsAccount.getBranchName());
        account.setIfscCode(savingsAccount.getIfscCode());
        account.setAccountType(savingsAccount.getAccountType());
        account.setBalance(savingsAccount.getBalance());
        account.setActive(savingsAccount.isActive());
    }
}
