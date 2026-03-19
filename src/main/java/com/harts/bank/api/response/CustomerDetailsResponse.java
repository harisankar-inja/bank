package com.harts.bank.api.response;

import com.harts.bank.api.request.dto.BankDetails;
import com.harts.bank.api.request.dto.ContactInfo;
import com.harts.bank.api.request.dto.PersonalInfo;
import lombok.Data;


@Data
public class CustomerDetailsResponse {

    private PersonalInfo personalInfo;
    private ContactInfo contactInfo;
    private BankDetails bankDetails;
}

