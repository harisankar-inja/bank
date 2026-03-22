package com.harts.bank.api.controller;

import com.harts.bank.api.request.LoanAccountRequest;
import com.harts.bank.api.response.LoanAccountResponse;
import com.harts.bank.service.impl.LoanAccountService;
import com.harts.bank.service.impl.LoanEligibilityResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loan-accounts")
//TODO: add bank name in the url as path variable and validate that the bank name in the request body matches the bank name in the url, also add loan type in the url as path variable and validate that the loan type in the request body matches the loan type in the url
@RequiredArgsConstructor
public class LoanAccountController {
    
    private final LoanAccountService loanAccountService;

    @PostMapping("/create-loan")
    public ResponseEntity<LoanAccountResponse> createLoanAccount(@Valid @RequestBody LoanAccountRequest accountRequest) {
        // TODO: validate bank details are correct, bank name, ifsc code should be valid and matching, also validate that the customer is eligible for the loan account based on the request details and existing accounts and loans
        return new ResponseEntity<>(
                loanAccountService.createAccount(accountRequest),
                HttpStatus.OK);
    }

    @GetMapping("/cif/{cif}")
    public ResponseEntity<List<LoanAccountResponse>> getAccountsByCustomerInfoFile(@PathVariable String cif) {
        return new ResponseEntity<>(
                loanAccountService.getAccountsByCustomerInfoFile(cif),
                HttpStatus.OK);
    }

    @GetMapping("/accountNumber/{accountNumber}")
    public ResponseEntity<LoanAccountResponse> getAccountByAccountNumber(@PathVariable String accountNumber) {
        return new ResponseEntity<>(
                loanAccountService.getAccountDetails(accountNumber),
                HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<LoanAccountResponse>> findAllAccounts(@RequestParam(required = false) String bankName) {
        return new ResponseEntity<>(
                loanAccountService.findAllAccounts(bankName),
                HttpStatus.OK);
    }

    @GetMapping("/all/loan-type")
    public ResponseEntity<List<LoanAccountResponse>> findAllAccountsByLoanType(@RequestParam String bankName,
                                                       @RequestParam String loanType) {
        return new ResponseEntity<>(
                loanAccountService.findAllAccountsByLoanType(bankName, loanType),
                HttpStatus.OK);
    }

    @GetMapping("/eligibility")
    public ResponseEntity<LoanEligibilityResponse> checkLoanEligibility(@Valid @RequestBody LoanAccountRequest accountRequest) {
        return new ResponseEntity<>(
                loanAccountService.checkLoanEligibility(accountRequest),
                HttpStatus.OK);
    }
}
