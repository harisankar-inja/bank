package com.harts.bank.api.request;

import com.harts.bank.enums.AccountType;
import com.harts.bank.model.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SavingsAccountRequest {

    // for savings account creation, these details are mandatory
    @NotNull
    private AccountType accountType;
    @NotBlank
    private String aadharNumber;
    @NotBlank
    private String panNumber;
    @NotBlank
    private String bankName;
    @NotBlank
    private String bankBranch;
    @NotBlank
    private String ifscCode;
    @NotNull
    private double initialDeposit;

    // customer details for new customers
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Address address;
}
