package com.harts.bank.service;

import com.harts.bank.api.response.CustomerResponse;
import com.harts.bank.repository.BankRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankService {

    private final BankRepo bankRepo;

    public List<CustomerResponse> getCustomersByBankName(String bankName) {
        return bankRepo.getCustomersByBankName(bankName);
    }
}
