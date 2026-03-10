package com.harts.bank.api.controller;

import com.harts.bank.api.request.AccountRequest;
import com.harts.bank.api.response.Account;
import com.harts.bank.service.impl.SavingsAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final SavingsAccountService savingsAccountService;

    @PostMapping("/savings")
    public ResponseEntity<Account> createSavingsAccount(@RequestBody AccountRequest accountRequest) {
        return new ResponseEntity<>(
                savingsAccountService.createAccount(accountRequest, true),
                HttpStatus.OK);
    }
}
