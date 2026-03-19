package com.harts.bank.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "loan-eligibility")
@Data
public class LoanEligibilityConfig {
    private List<Loan> loans;

    @Data
    public static class Loan {
        private String loanType;
        private int minCibilScore;
        private int maxCibilScore;
        private int minIncome;
        private int maxIncome;
        private double minInterestRate;
        private double maxInterestRate;
        private int minLoanTerm;
        private int maxLoanTerm;
        private int maxExistingEmis;
    }
}

