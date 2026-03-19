package com.harts.bank.service.impl;

import com.harts.bank.api.request.LoanAccountRequest;
import com.harts.bank.config.LoanEligibilityConfig;
import com.harts.bank.exceptions.InvalidRequestException;
import com.harts.bank.model.Customer;
import com.harts.bank.model.LoanAccount;
import com.harts.bank.model.SavingsAccount;
import com.harts.bank.service.LoanEligibilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.harts.bank.utils.CommonUtils.calculateMidCreditScore;

@Service
@RequiredArgsConstructor
public class LoanEligibilityServiceImpl implements LoanEligibilityService {
    
    @Autowired
    private LoanEligibilityConfig loanEligibilityConfig;
    private String loanType;
    private int exixtingEmis;
    
    @Override
    public LoanEligibilityResponse validateLoanAccountRequest(LoanAccountRequest request, Customer customer, SavingsAccount savingsAccount, List<LoanAccount> loanAccounts) {
        double monthlyIncome = request.getMonthlyIncome();
        int creditScore = request.getCreditScore();

        double totalMontlyEmis = findTotalMonthlyEmi(loanAccounts);
        double requestedAmount = request.getLoanAmountRequested();
        loanType = request.getSubAccountType().name();
        exixtingEmis = loanAccounts.size();

        if (creditScore < 650) {
            return new LoanEligibilityResponse(false, 0, "Credit score too low");
        }

        switch (loanType) {
            case "HOME_LOAN":
                if(requestedAmount > request.getAnnualIncome() * 3) {
                    return new LoanEligibilityResponse(false, 0, "Requested amount exceeds 3 times annual income");
                }
                return checkMonthlyIncomeVsEmis(request, monthlyIncome, totalMontlyEmis, requestedAmount);
            case "PERSONAL_LOAN":
                if(requestedAmount > request.getAnnualIncome() * 2) {
                    return new LoanEligibilityResponse(false, request.getAnnualIncome() * 2, "Requested amount exceeds eligibility based on annual income");
                }
                return checkMonthlyIncomeVsEmis(request, monthlyIncome, totalMontlyEmis, requestedAmount);
            case "EDUCATION_LOAN":
                // Todo: Add education loan specific eligibility logic, based on course, institution, etc. and previous education loan history
                // no service available for education loan eligibility check, so returning not eligible for now
                return new LoanEligibilityResponse(false, requestedAmount, "no service available for education loan");
            case "VEHICLE_LOAN":
                return checkMonthlyIncomeVsEmis(request, monthlyIncome, totalMontlyEmis, requestedAmount);
            case "BUSINESS_LOAN":
                return checkMonthlyIncomeVsEmis(request, monthlyIncome, totalMontlyEmis, requestedAmount);
            case "GOLD_LOAN":
                return checkMonthlyIncomeVsEmis(request, monthlyIncome, totalMontlyEmis, requestedAmount);
            default:
                return new LoanEligibilityResponse(false, 0, "Invalid loan type");
        }

    }

    private double findTotalMonthlyEmi(List<LoanAccount> loanAccounts) {
        if (loanAccounts == null || loanAccounts.isEmpty()) {
            return 0;
        } else {
            double totalExistingEmis = 0;
            for(LoanAccount loanAccount : loanAccounts) {
                if (loanAccount.isActive()) {
                    totalExistingEmis += loanAccount.getEmiAmount();
                }
            }
            return totalExistingEmis;
        }
    }

    private LoanEligibilityResponse checkMonthlyIncomeVsEmis(LoanAccountRequest request, double monthlyIncome, double totalMontlyEmis, double requestedAmount) {

        if (monthlyIncome < 20000) {
            return new LoanEligibilityResponse(false, 0, "Monthly income too low");
        }
        double monthlyIncomeAfterEmis = monthlyIncome - totalMontlyEmis;
        if (monthlyIncomeAfterEmis < (0.2 * monthlyIncome)) {
            return new LoanEligibilityResponse(false, 0, "Remaining income after EMIs is less than 20% of monthly income");
        }
        double maxEligible = (monthlyIncome - totalMontlyEmis) * 2.5;
        if (requestedAmount > maxEligible &&
                (!request.isRequestedLoanAmountAutoCal() || checkForRequestedAmountOverrideEligibility(request, requestedAmount, maxEligible))) {
            return new LoanEligibilityResponse(false, maxEligible, "Requested amount exceeds eligibility");
        }

        return new LoanEligibilityResponse(true, requestedAmount, "Eligible");
    }
    
    private boolean checkForRequestedAmountOverrideEligibility(LoanAccountRequest accountRequest, double requestedAmount, double eligibleAmount) {
        // If requested amount is greater than eligible amount, then we can check if the customer is eligible for the requested amount based on their credit score and income, even if they are not eligible for the requested amount based on the monthly income vs existing EMIs criteria. This is to give some flexibility to the customers who might have a good credit score and income but have high existing EMIs due to some temporary financial situation.
        Optional<LoanEligibilityConfig.Loan> loan = loanEligibilityConfig.getLoans().stream()
                .filter(l -> l.getLoanType().equalsIgnoreCase(loanType))
                .findFirst();
        if (loan.isEmpty()) {
            throw new InvalidRequestException("No loan eligibility configuration found for loan type: " + loanType);
        }
        int avgCredScore = calculateMidCreditScore(loan.get());
        boolean isEligibleForRequestedAmount = accountRequest.getCreditScore() >= avgCredScore
                && accountRequest.getMonthlyIncome() >= loan.get().getMinIncome()
                && exixtingEmis <= loan.get().getMaxExistingEmis()
                && requestedAmount < eligibleAmount * 3; // to prevent very high loan amount requests even if the customer has good credit score and income, we can set a cap on the requested amount to be at max 3 times the eligible amount based on monthly income vs existing EMIs criteria. This is to prevent customers from requesting very high loan amounts which they might not be able to repay even if they have good credit score and income.
        return !isEligibleForRequestedAmount;
    }
}
