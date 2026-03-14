package com.harts.bank.service.impl;

import com.harts.bank.api.request.LoanAccountRequest;
import com.harts.bank.api.response.LoanAccountResponse;
import com.harts.bank.enums.AccountType;
import com.harts.bank.exceptions.AccountNotFoundException;
import com.harts.bank.exceptions.CustomerNotFoundException;
import com.harts.bank.exceptions.LoanNotEligibleException;
import com.harts.bank.model.Customer;
import com.harts.bank.model.LoanAccount;
import com.harts.bank.model.SavingsAccount;
import com.harts.bank.repository.LoanAccountRepo;
import com.harts.bank.repository.SavingsAccountRepo;
import com.harts.bank.repository.CustomerRepo;
import com.harts.bank.service.AccountService;
import com.harts.bank.service.CustomerService;
import com.harts.bank.service.LoanEligibilityService;
import com.harts.bank.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanAccountService implements AccountService<LoanAccountRequest, LoanAccountResponse> {

    private final CustomerRepo customerRepo;
    private final CustomerService customerService;
    private final SavingsAccountRepo accountRepo;
    private final SavingsAccountService savingsAccountService;
    private final LoanEligibilityService loanEligibilityService;
    private final LoanAccountRepo loanAccountRepo;

    /***
     * Loan account creation is a two step process, first we need to create a savings account for the customer (if not already exists)
     * and then we can create a loan account for the customer. This is because we need to link the loan account to the savings account for the customer.
     * @param accountRequest
     * @return
     */
    @Override
    public LoanAccountResponse createAccount(LoanAccountRequest accountRequest) {
        Optional<Customer> customer = customerRepo.findByAdhaarNumWithBank(accountRequest.getAadharNumber(), accountRequest.getBankName());
        if(customer.isEmpty()){
            throw new CustomerNotFoundException("Customer with Aadhaar number " + accountRequest.getAadharNumber() +
                    " not found in bank " + accountRequest.getBankName() + ". Cannot create loan account.");
        } else if (!customer.get().isActive()){
            throw new CustomerNotFoundException("Customer with Aadhaar number " + accountRequest.getAadharNumber() +
                    " is not active in bank " + accountRequest.getBankName() + ". Cannot create loan account.");
        }
        Optional<SavingsAccount> savingsAccount = accountRepo.findByCustomerIdAndBankAndAccountType(
                customer.get().getCustomerId(), accountRequest.getBankName(), AccountType.SAVINGS);
        if(savingsAccount.isEmpty()) {
            throw new AccountNotFoundException("Customer with Aadhaar number " + accountRequest.getAadharNumber() +
                    " does not have a savings account in bank " + accountRequest.getBankName() + ". Cannot create loan account.");
        }
        // Validate loan eligibility
        List<LoanAccount> loanAccounts = loanAccountRepo.getLoanAccountsByPanNum(customer.get().getPanNumber());
        LoanEligibilityResponse eligibility = loanEligibilityService.validateLoanAccountRequest(accountRequest, customer.get(), savingsAccount.get(), loanAccounts);
        if (!eligibility.isEligible()) {
            throw new LoanNotEligibleException("Loan not eligible: " + eligibility.getMessage() + ". Eligible amount: " + eligibility.getEligibleAmount());
        }
        LoanAccountResponse account = createLoanAccount(accountRequest, customer.get(), savingsAccount.get(), eligibility.getEligibleAmount());

        return account;
    }

    private LoanAccountResponse createLoanAccount(LoanAccountRequest accountRequest, Customer customer, SavingsAccount savingsAccount, double eligibleAmount) {
        String loanAccountNum = CommonUtils.generateRandomNumber(8); // Todo: Implement logic to generate unique loan account number
        LoanAccount account = new LoanAccount();
        setLoanAccountModel(account, accountRequest, customer, loanAccountNum, savingsAccount.getAccountNumber(), eligibleAmount);
        loanAccountRepo.persist(account);

        LoanAccountResponse accountResponse = new LoanAccountResponse();
        mapLoanAccountRequestToResponse(accountResponse, accountRequest, customer, loanAccountNum);
        return accountResponse;
    }

    private void setLoanAccountModel(LoanAccount account, LoanAccountRequest accountRequest, Customer customer,
                                     String loanAccountNum, String savingsAccNum, double eligibleAmount) {
        account.setCif(customer.getCustomerId());
        account.setPanNumber(accountRequest.getPanNumber());
        account.setAccountHolderName(customer.getFirstName() + " " + customer.getLastName());
        account.setLoanAccountNumber(loanAccountNum); // Generate unique account number
        account.setLinkedSavingsAccountNumber(savingsAccNum);
        account.setAccountType(AccountType.LOAN);
        account.setSubAccountType(accountRequest.getSubAccountType());
        account.setLoanAmount(eligibleAmount);
        account.setEmiAmount(calculateEmi(eligibleAmount, accountRequest.getInterestRate(), accountRequest.getLoanTermInYears()));
        account.setPendingEmis(calulatePendingEmis(accountRequest.getLoanTermInYears()));
        account.setEmiDueDate(calculateEmiDueDate(LocalDate.now()));
        account.setInterestRate(accountRequest.getInterestRate());
        account.setLoanTermInYears(accountRequest.getLoanTermInYears());
        account.setCreditScore(accountRequest.getCreditScore());
        account.setAnnualIncome(accountRequest.getAnnualIncome());
        account.setExistingEmis(accountRequest.getExistingEmis());
        account.setEmploymentType(accountRequest.getEmploymentType().name());
        account.setActive(true);
        account.setLoanStartDate(calculateLoanStartDate());
        account.setLoanEndDate(calculateLoanEndDate(account.getLoanStartDate(), account.getLoanTermInYears()));
        account.setCreatedBy(accountRequest.getCreatedBy()==null? "SYSTEM" : accountRequest.getCreatedBy());
        account.setUpdatedBy(accountRequest.getUpdatedBy()==null? account.getCreatedBy() : accountRequest.getUpdatedBy());
    }

    private int calulatePendingEmis(int loanTermInYears) {
        return loanTermInYears * 12; // Assuming monthly EMIs, so total EMIs = loan term in years * 12
    }

    private double calculateEmi(double eligibleAmount, double interestRate, int loanTermInYears) {
        // EMI = [P x R x (1+R)^N]/[(1+R)^N-1]
        // P = eligibleAmount, R = monthly interest rate, N = loan term in months
        double monthlyInterestRate = interestRate / 12 / 100;
        int loanTermInMonths = loanTermInYears * 12;
        double emi = (eligibleAmount * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, loanTermInMonths)) /
                (Math.pow(1 + monthlyInterestRate, loanTermInMonths) - 1);
        return Math.round(emi * 100.0) / 100.0; // Round off to 2 decimal places
    }

    private LocalDate calculateLoanStartDate() {
        // Set loan start date to next month 5th to give some time for processing and disbursal
        LocalDate now = LocalDate.now();
        LocalDate nextMonth = now.plusMonths(1);
        return LocalDate.of(nextMonth.getYear(), nextMonth.getMonth(), 5);
    }

    private LocalDate calculateEmiDueDate(LocalDate now) {
        // Set EMI due date to 5th of next month
        LocalDate nextMonth = now.plusMonths(1);
        return LocalDate.of(nextMonth.getYear(), nextMonth.getMonth(), 5);
    }

    private LocalDate calculateLoanEndDate(LocalDate loanStartDate, int loanTermInYears) {
        return loanStartDate.plusYears(loanTermInYears);
    }

    private void mapLoanAccountRequestToResponse(LoanAccountResponse accountResponse, LoanAccountRequest accountRequest, Customer customer, String loanAccountNumber) {
        accountResponse.setAccountNumber(loanAccountNumber); // Generate unique account number
        accountResponse.setCif(customer.getCustomerId());
        accountResponse.setAccountHolderName(customer.getFirstName() + " " + customer.getLastName());
        accountResponse.setBankName(accountRequest.getBankName());
        accountResponse.setBranchName(accountRequest.getBankBranch());
        accountResponse.setIfscCode(accountRequest.getIfscCode());
        accountResponse.setAccountType(AccountType.LOAN);
        accountResponse.setSubAccountType(accountRequest.getSubAccountType());
        accountResponse.setLoanAmount(accountRequest.getLoanAmountRequested());
        accountResponse.setInterestRate(accountRequest.getInterestRate());
        accountResponse.setLoanTermInYears(accountRequest.getLoanTermInYears());
        accountResponse.setActive(true);
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
