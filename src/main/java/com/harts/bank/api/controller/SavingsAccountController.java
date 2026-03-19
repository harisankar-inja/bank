package com.harts.bank.api.controller;

import com.harts.bank.api.request.SavingsAccountRequest;
import com.harts.bank.api.response.SavingsAccountResponse;
import com.harts.bank.service.impl.SavingsAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class SavingsAccountController {

    private final SavingsAccountService savingsAccountService;

    @PostMapping("/savings")
    public ResponseEntity<SavingsAccountResponse> createSavingsAccount(@Valid @RequestBody SavingsAccountRequest accountRequest) {
        return new ResponseEntity<>(
                savingsAccountService.createAccount(accountRequest),
                HttpStatus.OK);
    }

    @GetMapping("/cif/{cif}")
    public ResponseEntity<?> getAccountsByCustomerInfoFile(@PathVariable String cif) {
        return new ResponseEntity<>(
                savingsAccountService.getAccountsByCustomerInfoFile(cif),
                HttpStatus.OK);
    }

    @GetMapping("/accountNumber/{accountNumber}")
    public ResponseEntity<?> getAccountByAccountNumber(@PathVariable String accountNumber) {
        return new ResponseEntity<>(
                savingsAccountService.getAccountDetails(accountNumber),
                HttpStatus.OK);
    }

//    @GetMapping("/all")
//    public ResponseEntity<?> findAllAccounts(@RequestParam(required = false) String bankName) {
//        return new ResponseEntity<>(
//                accountDetailsService.findAllAccounts(bankName),
//                HttpStatus.OK);
//    }
}
