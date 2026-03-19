package com.harts.bank.api.controller;

import com.harts.bank.api.response.CustomerDetailsResponse;
import com.harts.bank.service.AccountDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account/details")
@RequiredArgsConstructor
public class AccountDetailsController {

    private final AccountDetailsService accountDetailsService;

    @GetMapping("/aadhar")
    public ResponseEntity<List<CustomerDetailsResponse>> getALLAccountsByAadharNumber(@RequestParam String aadharNumber) {
        return new ResponseEntity<>(
                accountDetailsService.getAllAccountsByAadharNumber(aadharNumber),
                HttpStatus.OK);
    }

    @GetMapping("/pan")
    public ResponseEntity<List<CustomerDetailsResponse>> getLoanAccountsByPanNumber(@RequestParam String panNumber) {
        return new ResponseEntity<>(
                accountDetailsService.getLoanAccountsByPanNumber(panNumber),
                HttpStatus.OK);
    }
}
