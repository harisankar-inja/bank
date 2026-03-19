package com.harts.bank.utils;

import com.harts.bank.config.LoanEligibilityConfig;

public class CommonUtils {

    public static String generateRandomNumber(int length) {
        StringBuilder accountNumber = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int digit = (int) (Math.random() * 10);
            accountNumber.append(digit);
        }
        return accountNumber.toString();
    }

    /**
     * Returns the mid value (median) between min and max CIBIL score for the loan.
     */
    public static int calculateMidCreditScore(LoanEligibilityConfig.Loan loan) {
        int min = loan.getMinCibilScore();
        int max = loan.getMaxCibilScore();
        return min + (max - min) / 2;
    }
}
