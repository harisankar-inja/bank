package com.harts.bank.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerResponse {

    private String customerId; //cif
    private String bankName;
    private String firstName;
    private String lastName;
}
