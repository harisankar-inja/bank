package com.harts.bank.service;

import com.harts.bank.api.request.dto.BankDetails;
import com.harts.bank.api.request.dto.ContactInfo;
import com.harts.bank.api.request.dto.PersonalInfo;
import com.harts.bank.api.response.CustomerDetailsResponse;
import com.harts.bank.api.response.CustomerResponse;
import com.harts.bank.model.CustomerDetails;
import com.harts.bank.repository.AccountDetailsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountDetailsService {

    private final AccountDetailsRepo accountDetailsRepo;

    public List<CustomerDetailsResponse> getAllAccountsByAadharNumber(String aadharNumber) {
        return mapCustomerDetailsResponse(accountDetailsRepo.getAccountsByAadharNumber(aadharNumber), false);
    }

    public List<CustomerDetailsResponse> getLoanAccountsByPanNumber(String panNumber) {
        return mapCustomerDetailsResponse(accountDetailsRepo.getLoanAccountsByPanNumber(panNumber), true);
    }

    private List<CustomerDetailsResponse> mapCustomerDetailsResponse(List<CustomerDetails> customerDetailsList, boolean isLoanAccount) {
        return customerDetailsList.stream().map(customerDetails -> {
            CustomerDetailsResponse response = new CustomerDetailsResponse();

            BankDetails bankDetails = new BankDetails();
            bankDetails.setCif(customerDetails.getCustomerId());
            bankDetails.setBankName(customerDetails.getBankName());
            bankDetails.setAccounts(isLoanAccount ? customerDetails.getAccounts() : customerDetails.getLoanAccounts());
            response.setBankDetails(bankDetails);

            ContactInfo contactInfo = new ContactInfo();
            contactInfo.setEmail(customerDetails.getEmail());
            contactInfo.setPhoneNumber(customerDetails.getPhoneNumber());
            response.setContactInfo(contactInfo);

            PersonalInfo personalInfo = new PersonalInfo();
            personalInfo.setFirstName(customerDetails.getFirstName());
            personalInfo.setLastName(customerDetails.getLastName());
            personalInfo.setAadharNumber(customerDetails.getAdhaarNumber());
            personalInfo.setPanNumber(customerDetails.getPanNumber());
            response.setPersonalInfo(personalInfo);

            return response;
        }).toList();
    }
}
