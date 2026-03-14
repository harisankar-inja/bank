package com.harts.bank.service.impl;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoanEligibilityResponse {
    private boolean eligible;
    private double eligibleAmount;
    private String message;
}

