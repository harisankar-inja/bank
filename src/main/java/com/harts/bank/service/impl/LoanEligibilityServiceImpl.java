package com.harts.bank.service.impl;

import com.harts.bank.api.request.LoanAccountRequest;
import com.harts.bank.model.Customer;
import com.harts.bank.model.SavingsAccount;
import com.harts.bank.service.LoanEligibilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanEligibilityServiceImpl implements LoanEligibilityService {
    @Override
    public LoanEligibilityResponse validateLoanAccountRequest(LoanAccountRequest request, Customer customer, SavingsAccount savingsAccount) {
        double monthlyIncome = request.getMonthlyIncome();
        int creditScore = request.getCreditScore();

        //Todo: Fetch existing EMIs for the customer from DB or calculate based on existing loans
        double existingEmis = request.getExistingEmis();
        double requestedAmount = request.getLoanAmountRequested();
        String loanType = request.getSubAccountType().name();

        if (creditScore < 650) {
            return new LoanEligibilityResponse(false, 0, "Credit score too low");
        }

        switch (loanType) {
            case "HOME_LOAN":
                return checkMonthlyIncomeVsEmis(monthlyIncome * 4, existingEmis, requestedAmount);
            case "PERSONAL_LOAN":
                return checkMonthlyIncomeVsEmis(monthlyIncome, existingEmis, requestedAmount);
            case "EDUCATION_LOAN":
                // Todo: Add education loan specific eligibility logic, based on course, institution, etc. and previous education loan history
                // no service available for education loan eligibility check, so returning not eligible for now
                return new LoanEligibilityResponse(false, requestedAmount, "no service available for education loan");
//                break;
            case "VEHICLE_LOAN":
                return checkMonthlyIncomeVsEmis(monthlyIncome * 1.5, existingEmis, requestedAmount);
            case "BUSINESS_LOAN":
                return checkMonthlyIncomeVsEmis(monthlyIncome * 3, existingEmis, requestedAmount);
            case "GOLD_LOAN":
                return checkMonthlyIncomeVsEmis(monthlyIncome * 2, existingEmis, requestedAmount);
            default:
                return new LoanEligibilityResponse(false, 0, "Invalid loan type");
        }

    }

    private LoanEligibilityResponse checkMonthlyIncomeVsEmis(double monthlyIncome, double existingEmis, double requestedAmount) {

        if (monthlyIncome < 20000) {
            return new LoanEligibilityResponse(false, 0, "Monthly income too low");
        }
        double maxEligible = (monthlyIncome - existingEmis) * 2.5;
        if (requestedAmount > maxEligible) {
            return new LoanEligibilityResponse(false, maxEligible, "Requested amount exceeds eligibility");
        }

        return new LoanEligibilityResponse(true, requestedAmount, "Eligible");
    }
}

