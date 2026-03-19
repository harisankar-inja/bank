package com.harts.bank.api.request.dto;

import lombok.Data;

@Data
public class PersonalInfo {

    private String firstName;
    private String lastName;
    private String aadharNumber;
    private String panNumber;
}
