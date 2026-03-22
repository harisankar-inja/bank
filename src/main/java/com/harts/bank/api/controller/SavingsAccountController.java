package com.harts.bank.api.controller;

import com.harts.bank.api.request.SavingsAccountRequest;
import com.harts.bank.api.response.SavingsAccountResponse;
import com.harts.bank.service.impl.SavingsAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/savings-account")
@RequiredArgsConstructor
public class SavingsAccountController {

    private final SavingsAccountService savingsAccountService;

    @PostMapping("/create-savings")
    public ResponseEntity<SavingsAccountResponse> createSavingsAccount(@Valid @RequestBody SavingsAccountRequest accountRequest) {
        return new ResponseEntity<>(
                savingsAccountService.createAccount(accountRequest),
                HttpStatus.OK);
    }

    @GetMapping("/cif/{cif}")
    public ResponseEntity<List<SavingsAccountResponse>> getAccountsByCustomerInfoFile(@PathVariable String cif) {
        return new ResponseEntity<>(
                savingsAccountService.getAccountsByCustomerInfoFile(cif),
                HttpStatus.OK);
    }

    @GetMapping("/accountNumber/{accountNumber}")
    public ResponseEntity<SavingsAccountResponse> getAccountByAccountNumber(@PathVariable String accountNumber) {
        return new ResponseEntity<>(
                savingsAccountService.getAccountDetails(accountNumber),
                HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<SavingsAccountResponse>> findAllAccounts(@RequestParam(required = false) String bankName) {
        return new ResponseEntity<>(
                savingsAccountService.findAllAccounts(bankName),
                HttpStatus.OK);
    }
}
