package com.harts.bank.api.controller;

import com.harts.bank.model.Address;
import com.harts.bank.model.Customer;
import com.harts.bank.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@Valid @RequestBody Customer customer) {
        customerService.registerCustomer(customer);
        return new ResponseEntity<>("Customer registered successfully", HttpStatus.OK);
    }

    @PutMapping("/update/name/{firstName}/{lastName}")
    public ResponseEntity<String> updateCustomerName(@PathVariable String firstName, @PathVariable String lastName, @RequestParam String customerId) {
        customerService.updateCustomerName(customerId, firstName, lastName);
        return new ResponseEntity<>("Customer name updated successfully", HttpStatus.OK);
    }

    @PutMapping("/update/address")
    public ResponseEntity<String> updateCustomerAddress(@RequestParam String customerId, @RequestBody Address address) {
        customerService.updateCustomerAddress(customerId, address);
        return new ResponseEntity<>("Customer address updated successfully", HttpStatus.OK);
    }

    @PutMapping("/update/contact-info")
    public ResponseEntity<String> updateCustomerContactInfo(@RequestParam String customerId, @RequestParam String phoneNumber, @RequestParam String email) {
        customerService.updateCustomerContactInfo(customerId, phoneNumber, email);
        return new ResponseEntity<>("Customer contact information updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity<String> deleteCustomer(@PathVariable String customerId) {
        customerService.deleteCustomer(customerId);
        return new ResponseEntity<>("Customer deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/findByCif/{customerId}")
    public ResponseEntity<Customer> getCustomer(@PathVariable String customerId) {
        return new ResponseEntity<>(customerService.getCustomerById(customerId), HttpStatus.OK);
    }

    @GetMapping("/findByAdhaar/{adhaarNumber}")
    public ResponseEntity<Customer> getCustomerByAdhaar(@PathVariable String adhaarNumber) {
        return new ResponseEntity<>(customerService.getCustomerByAdhaar(adhaarNumber), HttpStatus.OK);
    }

    @GetMapping("/findByPan/{panNumber}")
    public ResponseEntity<Customer> getCustomerByPan(@PathVariable String panNumber) {
        return new ResponseEntity<>(customerService.getCustomerByPan(panNumber), HttpStatus.OK);
    }

    @GetMapping("/findByName")
    public ResponseEntity<List<Customer>> getCustomerByName(@RequestParam String firstName, @RequestParam String lastName) {
        return new ResponseEntity<>(customerService.getCustomerByName(firstName, lastName), HttpStatus.OK);
    }
}
