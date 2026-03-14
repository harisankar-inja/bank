package com.harts.bank.service.impl;

import com.harts.bank.api.request.LoanAccountRequest;
import com.harts.bank.model.Customer;
import com.harts.bank.model.LoanAccount;
import com.harts.bank.model.SavingsAccount;
import com.harts.bank.service.LoanEligibilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanEligibilityServiceImpl implements LoanEligibilityService {
    @Override
    public LoanEligibilityResponse validateLoanAccountRequest(LoanAccountRequest request, Customer customer, SavingsAccount savingsAccount, List<LoanAccount> loanAccounts) {
        double monthlyIncome = request.getMonthlyIncome();
        int creditScore = request.getCreditScore();

        //Todo: Fetch existing EMIs for the customer from DB or calculate based on existing loans
        double totalMontlyEmis = findTotalMontlyEmis(loanAccounts);
        double requestedAmount = request.getLoanAmountRequested();
        String loanType = request.getSubAccountType().name();

        if (creditScore < 650) {
            return new LoanEligibilityResponse(false, 0, "Credit score too low");
        }

        switch (loanType) {
            case "HOME_LOAN":
                return checkMonthlyIncomeVsEmis(monthlyIncome * 4, totalMontlyEmis, requestedAmount);
            case "PERSONAL_LOAN":
                return checkMonthlyIncomeVsEmis(monthlyIncome, totalMontlyEmis, requestedAmount);
            case "EDUCATION_LOAN":
                // Todo: Add education loan specific eligibility logic, based on course, institution, etc. and previous education loan history
                // no service available for education loan eligibility check, so returning not eligible for now
                return new LoanEligibilityResponse(false, requestedAmount, "no service available for education loan");
            case "VEHICLE_LOAN":
                return checkMonthlyIncomeVsEmis(monthlyIncome * 1.5, totalMontlyEmis, requestedAmount);
            case "BUSINESS_LOAN":
                return checkMonthlyIncomeVsEmis(monthlyIncome * 3, totalMontlyEmis, requestedAmount);
            case "GOLD_LOAN":
                return checkMonthlyIncomeVsEmis(monthlyIncome * 2, totalMontlyEmis, requestedAmount);
            default:
                return new LoanEligibilityResponse(false, 0, "Invalid loan type");
        }

    }

    private double findTotalMontlyEmis(List<LoanAccount> loanAccounts) {
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

    private LoanEligibilityResponse checkMonthlyIncomeVsEmis(double monthlyIncome, double totalMontlyEmis, double requestedAmount) {

        if (monthlyIncome < 20000) {
            return new LoanEligibilityResponse(false, 0, "Monthly income too low");
        }
        double maxEligible = (monthlyIncome - totalMontlyEmis) * 2.5;
        if (requestedAmount > maxEligible) {
            return new LoanEligibilityResponse(false, maxEligible, "Requested amount exceeds eligibility");
        }

        return new LoanEligibilityResponse(true, requestedAmount, "Eligible");
    }
}

