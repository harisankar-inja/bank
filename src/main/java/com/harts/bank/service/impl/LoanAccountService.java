package com.harts.bank.service.impl;

import com.harts.bank.api.request.LoanAccountRequest;
import com.harts.bank.api.response.LoanAccountResponse;
import com.harts.bank.config.BankBranchesConfig;
import com.harts.bank.config.LoanEligibilityConfig;
import com.harts.bank.enums.AccountType;
import com.harts.bank.exceptions.AccountNotFoundException;
import com.harts.bank.exceptions.CustomerNotFoundException;
import com.harts.bank.exceptions.InvalidRequestException;
import com.harts.bank.exceptions.LoanNotEligibleException;
import com.harts.bank.model.Customer;
import com.harts.bank.model.LoanAccount;
import com.harts.bank.model.SavingsAccount;
import com.harts.bank.repository.LoanAccountRepo;
import com.harts.bank.repository.SavingsAccountRepo;
import com.harts.bank.repository.CustomerRepo;
import com.harts.bank.service.AccountService;
import com.harts.bank.service.LoanEligibilityService;
import com.harts.bank.utils.CommonUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.harts.bank.utils.CommonUtils.calculateMidCreditScore;

@Service
@RequiredArgsConstructor
public class LoanAccountService implements AccountService<LoanAccountRequest, LoanAccountResponse> {

    private final CustomerRepo customerRepo;
    private final SavingsAccountRepo savingsAccountRepo;
    private final LoanEligibilityService loanEligibilityService;
    private final LoanAccountRepo loanAccountRepo;

    @Autowired
    private BankBranchesConfig bankBranchesConfig;
    @Autowired
    private LoanEligibilityConfig loanEligibilityConfig;
    private int existingEmis;

    /***
     * Loan account creation is a two step process, first we need to create a savings account for the customer (if not already exists)
     * and then we can create a loan account for the customer. This is because we need to link the loan account to the savings account for the customer.
     * @param accountRequest
     * @return
     */
    @Override
    public LoanAccountResponse createAccount(LoanAccountRequest accountRequest) {
        validateBankDetails(accountRequest.getBankName(), accountRequest.getBankBranch(), accountRequest.getIfscCode());
        Optional<Customer> customer = customerRepo.findByAdhaarNumWithBank(accountRequest.getAadharNumber(), accountRequest.getBankName());
        if(customer.isEmpty()){
            throw new CustomerNotFoundException("Customer with Aadhaar number " + accountRequest.getAadharNumber() +
                    " not found in bank " + accountRequest.getBankName() + ". Cannot create loan account.");
        } else if (!customer.get().isActive()){
            throw new CustomerNotFoundException("Customer with Aadhaar number " + accountRequest.getAadharNumber() +
                    " is not active in bank " + accountRequest.getBankName() + ". Cannot create loan account.");
        }
        Optional<SavingsAccount> savingsAccount = savingsAccountRepo.findByCustomerIdAndBankAndAccountType(
                customer.get().getCustomerId(), accountRequest.getBankName(), AccountType.SAVINGS);
        if(savingsAccount.isEmpty()) {
            throw new AccountNotFoundException("Customer with Aadhaar number " + accountRequest.getAadharNumber() +
                    " does not have a savings account in bank " + accountRequest.getBankName() + ". Cannot create loan account.");
        }
        List<LoanAccount> loanAccounts = loanAccountRepo.getLoanAccountsByPanNum(customer.get().getPanNumber());
        LoanEligibilityResponse eligibility = loanEligibilityService.validateLoanAccountRequest(accountRequest, customer.get(), savingsAccount.get(), loanAccounts);
        if (!eligibility.isEligible()) {
            throw new LoanNotEligibleException("Loan not eligible: " + eligibility.getMessage() + ". Eligible amount: " + eligibility.getEligibleAmount());
        }
        existingEmis = loanAccounts.size();
        return createLoanAccount(accountRequest, customer.get(), savingsAccount.get(), eligibility.getEligibleAmount());
    }

    private void validateBankDetails(@NotBlank String bankName, @NotBlank String bankBranch, @NotBlank String ifscCode) {
        if (bankBranchesConfig.getBanks() == null) {
            throw new IllegalStateException("Bank branches configuration not loaded. Please check your YAML and application.yml import.");
        }
        boolean valid = bankBranchesConfig.getBanks().stream()
                .filter(b -> b.getName().equalsIgnoreCase(bankName))
                .flatMap(b -> b.getBranches().stream())
                .anyMatch(br -> br.getName().equalsIgnoreCase(bankBranch) && br.getIfsc().equalsIgnoreCase(ifscCode));
        if (!valid) {
            throw new InvalidRequestException("Invalid bank/branch/IFSC combination for bank: " + bankName + ", branch: " + bankBranch + ", IFSC: " + ifscCode);
        }
    }

    private LoanAccountResponse createLoanAccount(LoanAccountRequest accountRequest, Customer customer, SavingsAccount savingsAccount, double eligibleAmount) {
        String loanAccountNum = CommonUtils.generateRandomNumber(8); // Todo: Implement logic to generate unique loan account number
        LoanAccount account = new LoanAccount();
        if(accountRequest.isInterestRateAndTermAutoCal()) {
            Optional<LoanEligibilityConfig.Loan> loan = loanEligibilityConfig.getLoans().stream()
                    .filter(l -> l.getLoanType().equalsIgnoreCase(accountRequest.getLoanType().name()))
                    .findFirst();
            if (loan.isEmpty()) {
                throw new InvalidRequestException("No loan eligibility configuration found for loan type: " + accountRequest.getLoanType().name());
            }
            accountRequest.setInterestRate(findLoanInterestRate(accountRequest, loan.get()));
            accountRequest.setLoanTermInYears(findLoanTerm(accountRequest, loan.get()));
        }
        setLoanAccountModel(account, accountRequest, customer, loanAccountNum, savingsAccount.getAccountNumber(), eligibleAmount);
        loanAccountRepo.persist(account);

        LoanAccountResponse accountResponse = new LoanAccountResponse();
        mapLoanAccountRequestToResponse(accountResponse, accountRequest, customer, loanAccountNum);
        return accountResponse;
    }

    private int findLoanTerm(LoanAccountRequest accountRequest, LoanEligibilityConfig.Loan loan) {
        int avgCredScore = calculateMidCreditScore(loan);
        return accountRequest.getCreditScore() >= avgCredScore ? loan.getMaxLoanTerm() : loan.getMinLoanTerm();
    }

    private double findLoanInterestRate(LoanAccountRequest accountRequest, LoanEligibilityConfig.Loan loan) {
        int avgCredScore = calculateMidCreditScore(loan);
        return accountRequest.getCreditScore() >= avgCredScore ? loan.getMinInterestRate() : loan.getMaxInterestRate();
    }

    private void setLoanAccountModel(LoanAccount account, LoanAccountRequest accountRequest, Customer customer,
                                     String loanAccountNum, String savingsAccNum, double eligibleAmount) {
        account.setCif(customer.getCustomerId());
        account.setBankName(accountRequest.getBankName());
        account.setPanNumber(accountRequest.getPanNumber());
        account.setAccountHolderName(customer.getFirstName() + " " + customer.getLastName());
        account.setLoanAccountNumber(loanAccountNum); // Generate unique account number
        account.setLinkedSavingsAccountNumber(savingsAccNum);
        account.setAccountType(AccountType.LOAN);
        account.setLoanType(accountRequest.getLoanType());
        account.setLoanAmount(eligibleAmount);
        account.setEmiAmount(calculateEmi(eligibleAmount, accountRequest.getInterestRate(), accountRequest.getLoanTermInYears()));
        account.setPendingEmis(calculatePendingEmis(accountRequest.getLoanTermInYears()));
        account.setEmiDueDate(calculateEmiDueDate(LocalDate.now()));
        account.setInterestRate(accountRequest.getInterestRate());
        account.setLoanTermInYears(accountRequest.getLoanTermInYears());
        account.setCreditScore(accountRequest.getCreditScore());
        account.setAnnualIncome(accountRequest.getAnnualIncome());
        account.setExistingEmis(existingEmis);
        account.setEmploymentType(accountRequest.getEmploymentType().name());
        account.setActive(true);
        account.setLoanStartDate(calculateLoanStartDate());
        account.setLoanEndDate(calculateLoanEndDate(account.getLoanStartDate(), account.getLoanTermInYears()));
        account.setCreatedBy(accountRequest.getCreatedBy()==null? "SYSTEM" : accountRequest.getCreatedBy());
        account.setUpdatedBy(accountRequest.getUpdatedBy()==null? account.getCreatedBy() : accountRequest.getUpdatedBy());
    }

    private int calculatePendingEmis(int loanTermInYears) {
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
//        accountResponse.setIfscCode(accountRequest.getIfscCode());
        accountResponse.setAccountType(AccountType.LOAN);
        accountResponse.setLoanType(accountRequest.getLoanType());
        accountResponse.setLoanAmount(accountRequest.getLoanAmountRequested());
        accountResponse.setInterestRate(accountRequest.getInterestRate());
        accountResponse.setLoanTermInYears(accountRequest.getLoanTermInYears());
        accountResponse.setActive(true);
    }

    @Override
    public List<LoanAccountResponse> getAccountsByCustomerInfoFile(String cif) {
        List<LoanAccount> loanAccounts = loanAccountRepo.getAccountsByCID(cif);
        if (loanAccounts.isEmpty()) {
            throw new AccountNotFoundException("No loan accounts found for customer with CIF " + cif);
        }
        return getLoanAccountResponses(loanAccounts);
    }

    @Override
    public LoanAccountResponse getAccountDetails(String accountNumber) {
        Optional<LoanAccount> loanAccount = loanAccountRepo.findByAccountNumber(accountNumber);
        if(loanAccount.isEmpty()) {
            throw new AccountNotFoundException("Loan account with account number " + accountNumber + " not found.");
        }
        LoanAccountResponse loanAccountResponse = new LoanAccountResponse();
        setLoanAccountResponse(loanAccountResponse, loanAccount.get());
        return loanAccountResponse;
    }

    private void setLoanAccountResponse(LoanAccountResponse loanAccountResponse, LoanAccount loanAccount) {
        loanAccountResponse.setAccountNumber(loanAccount.getLoanAccountNumber());
        loanAccountResponse.setCif(loanAccount.getCif());
        loanAccountResponse.setAccountHolderName(loanAccount.getAccountHolderName());
        loanAccountResponse.setBankName(loanAccount.getBankName());
        loanAccountResponse.setBranchName(bankBranchesConfig.getBanks().stream()
                .filter(b -> b.getName().equalsIgnoreCase(loanAccount.getBankName()))
                .flatMap(b -> b.getBranches().stream())
//                .filter(br -> br.getIfsc().equalsIgnoreCase(loanAccount.getIfscCode()))
                .findFirst()
                .map(BankBranchesConfig.Branch::getName)
                .orElse(null));
//        loanAccountResponse.setIfscCode(loanAccount.getIfscCode());
        loanAccountResponse.setAccountType(loanAccount.getAccountType());
        loanAccountResponse.setLoanType(loanAccount.getLoanType());
        loanAccountResponse.setLoanAmount(loanAccount.getLoanAmount());
        loanAccountResponse.setInterestRate(loanAccount.getInterestRate());
        loanAccountResponse.setLoanTermInYears(loanAccount.getLoanTermInYears());
        loanAccountResponse.setActive(loanAccount.isActive());
    }

    public List<LoanAccountResponse> findAllAccounts(String bankName) {
        List<LoanAccount> loanAccounts = loanAccountRepo.findAllAccounts(bankName);
        if (loanAccounts.isEmpty()) {
            throw new AccountNotFoundException("No loan accounts found for bank " + bankName);
        }
        return getLoanAccountResponses(loanAccounts);
    }

    private List<LoanAccountResponse> getLoanAccountResponses(List<LoanAccount> loanAccounts) {
        return loanAccounts.stream().map(la -> {
            LoanAccountResponse response = new LoanAccountResponse();
            setLoanAccountResponse(response, la);
            return response;
        }).toList();
    }


    public List<LoanAccountResponse> findAllAccountsByLoanType(String bankName, String loanType) {
        List<LoanAccount> loanAccounts = loanAccountRepo.findAllAccounts(bankName).stream()
                .filter(a -> a.getLoanType().name().equalsIgnoreCase(loanType))
                .toList();
        return getLoanAccountResponses(loanAccounts);
    }

    public LoanEligibilityResponse checkLoanEligibility(@Valid LoanAccountRequest accountRequest) {
        Optional<Customer> customer = customerRepo.findByAdhaarNumWithBank(accountRequest.getAadharNumber(), accountRequest.getBankName());
        if(customer.isEmpty()){
            throw new CustomerNotFoundException("Customer with Aadhaar number " + accountRequest.getAadharNumber() +
                    " not found in bank " + accountRequest.getBankName() + ". Cannot check loan eligibility.");
        } else if (!customer.get().isActive()){
            throw new CustomerNotFoundException("Customer with Aadhaar number " + accountRequest.getAadharNumber() +
                    " is not active in bank " + accountRequest.getBankName() + ". Cannot check loan eligibility.");
        }
        Optional<SavingsAccount> savingsAccount = savingsAccountRepo.findByCustomerIdAndBankAndAccountType(
                customer.get().getCustomerId(), accountRequest.getBankName(), AccountType.SAVINGS);
        if(savingsAccount.isEmpty()) {
            throw new AccountNotFoundException("Customer with Aadhaar number " + accountRequest.getAadharNumber() +
                    " does not have a savings account in bank " + accountRequest.getBankName() + ". Cannot check loan eligibility.");
        }
        List<LoanAccount> loanAccounts = loanAccountRepo.getLoanAccountsByPanNum(customer.get().getPanNumber());
        return loanEligibilityService.validateLoanAccountRequest(accountRequest, customer.get(), savingsAccount.get(), loanAccounts);
    }
}
