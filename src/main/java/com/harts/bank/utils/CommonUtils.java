package com.harts.bank.utils;

public class CommonUtils {

    public static String generateRandomNumber(int length) {
        StringBuilder accountNumber = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int digit = (int) (Math.random() * 10);
            accountNumber.append(digit);
        }
        return accountNumber.toString();
    }
}
