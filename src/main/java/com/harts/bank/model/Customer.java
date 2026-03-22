package com.harts.bank.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class Customer {

    private String customerId; //cif
    @NotBlank
    private String bankName;
    @NotBlank
    private String firstName;
    private String lastName;
    @NotBlank
    private String email;
    @NotBlank
    private String phoneNumber;
    @NotBlank
    private String adhaarNumber;
    @NotBlank
    private String panNumber;
    private Address address;
    @JsonProperty("customerActive")
    private boolean isActive;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
