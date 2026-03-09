package com.harts.bank.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Customer {

    private String customerId; //cif
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String adhaarNumber;
    private String panNumber;
    private Address address;
    private boolean isActive;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
