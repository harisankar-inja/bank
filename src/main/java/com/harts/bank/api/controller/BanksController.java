package com.harts.bank.api.controller;

import com.harts.bank.api.response.CustomerDetailsResponse;
import com.harts.bank.api.response.CustomerResponse;
import com.harts.bank.service.AccountDetailsService;
import com.harts.bank.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/banks")
@RequiredArgsConstructor
public class BanksController {

    private final AccountDetailsService accountDetailsService;
    private final BankService bankService;

    @GetMapping("/account/details/aadhar")
    public ResponseEntity<List<CustomerDetailsResponse>> getALLAccountsByAadharNumber(@RequestParam String aadharNumber) {
        return new ResponseEntity<>(
                accountDetailsService.getAllAccountsByAadharNumber(aadharNumber),
                HttpStatus.OK);
    }

    @GetMapping("/account/details/pan")
    public ResponseEntity<List<CustomerDetailsResponse>> getLoanAccountsByPanNumber(@RequestParam String panNumber) {
        return new ResponseEntity<>(
                accountDetailsService.getLoanAccountsByPanNumber(panNumber),
                HttpStatus.OK);
    }

    @GetMapping("/getCustomers")
    public ResponseEntity<List<CustomerResponse>> getCustomersByBankName(@RequestParam String bankName) {
        return new ResponseEntity<>(
                bankService.getCustomersByBankName(bankName),
                HttpStatus.OK);
    }
}
