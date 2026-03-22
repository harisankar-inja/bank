package com.harts.bank.service;

import com.harts.bank.exceptions.CustomerNotFoundException;
import com.harts.bank.exceptions.UniqueConstraintException;
import com.harts.bank.model.Address;
import com.harts.bank.model.Customer;
import com.harts.bank.repository.CustomerRepo;
import com.harts.bank.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepo customerRepo;

    public void registerCustomer(Customer customer, boolean checkForExistingCustomer) {
        if(checkForExistingCustomer) {
            Optional<Customer> cust = customerRepo.findByAdhaarNum(customer.getAdhaarNumber());
            if (cust.isPresent()) {
                throw new UniqueConstraintException("Customer with ID " + customer.getCustomerId() + " already exists");
            }
        }
        customer.setActive(true);
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());
        customer.setCreatedBy(customer.getCreatedBy() == null ? "system" : customer.getCreatedBy());
        customer.setUpdatedBy(customer.getUpdatedBy() == null ? "system" : customer.getUpdatedBy());
        customer.setCustomerId("CIF" + CommonUtils.generateRandomNumber(6));
        customerRepo.persist(customer);
    }

    public void deleteCustomer(String customerId) {
        Optional<Customer> customer = customerRepo.findByCif(customerId);
        if (customer.isPresent()) {
            customerRepo.delete(customerId);
        } else {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found");
        }
    }

    public Customer getCustomerById(String customerId) {
        Optional<Customer> customer = customerRepo.findByCif(customerId);
        if (customer.isPresent()) {
            return customer.get();
        } else {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found");
        }
    }


    public Customer getCustomerByAdhaar(String adhaarNumber) {
        Optional<Customer> customer = customerRepo.findByAdhaarNum(adhaarNumber);
        if (customer.isPresent()) {
            return customer.get();
        } else {
            throw new CustomerNotFoundException("Customer with Adhaar number " + adhaarNumber + " not found");
        }
    }

    public Customer getCustomerByPan(String panNumber) {
        Optional<Customer> customer = customerRepo.findByPanNum(panNumber);
        if (customer.isPresent()) {
            return customer.get();
        } else {
            throw new CustomerNotFoundException("Customer with PAN number " + panNumber + " not found");
        }
    }

    public List<Customer> getCustomerByName(String firstName, String lastName) {
        List<Customer> customers = customerRepo.findByFirstNameAndLastName(firstName, lastName);
        if (!customers.isEmpty()) {
            return customers;
        } else {
            throw new CustomerNotFoundException("Customer with name " + firstName + " " + lastName + " not found");
        }
    }

    public void updateCustomerName(String customerId, String firstName, String lastName) {
        Optional<Customer> customerOpt = customerRepo.findByCif(customerId);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            customer.setUpdatedAt(LocalDateTime.now());
            customer.setUpdatedBy("system");
            customerRepo.update(customer);
        } else {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found");
        }
    }

    public void updateCustomerAddress(String customerId, Address address) {
        Optional<Customer> customerOpt = customerRepo.findByCif(customerId);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            customer.setAddress(address);
            customer.setUpdatedAt(LocalDateTime.now());
            customer.setUpdatedBy("system");
            customerRepo.update(customer);
        } else {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found");
        }
    }

    public void updateCustomerContactInfo(String customerId, String phoneNumber, String email) {
        Optional<Customer> customerOpt = customerRepo.findByCif(customerId);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            customer.setPhoneNumber(phoneNumber);
            customer.setEmail(email);
            customer.setUpdatedAt(LocalDateTime.now());
            customer.setUpdatedBy("system");
            customerRepo.update(customer);
        } else {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found");
        }
    }

    public void activateCustomer(String customerId) {
        Optional<Customer> customerOpt = customerRepo.findByCif(customerId);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            customer.setActive(true);
            customer.setUpdatedAt(LocalDateTime.now());
            customer.setUpdatedBy("system");
            customerRepo.update(customer);
        } else {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found");
        }
    }

    public void deactivateCustomer(String customerId) {
        Optional<Customer> customerOpt = customerRepo.findByCif(customerId);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            customer.setActive(false);
            customer.setUpdatedAt(LocalDateTime.now());
            customer.setUpdatedBy("system");
            customerRepo.update(customer);
        } else {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found");
        }
    }
}