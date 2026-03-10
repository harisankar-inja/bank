package com.harts.bank.api.request.validation;

import com.harts.bank.api.request.SavingsAccountRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

//not being used, as per the current requirements, but can be used in future if we want to validate the new customer
public class ValidateNewCustomerFieldsValidator implements ConstraintValidator<ValidateNewCustomerFields, SavingsAccountRequest> {
    @Override
    public boolean isValid(SavingsAccountRequest request, ConstraintValidatorContext context) {
        if (request == null) return true;
//        if (!request.isNewCustomer) return true;

        boolean valid = true;
        context.disableDefaultConstraintViolation();

        if (isBlank(request.getFirstName())) {
            context.buildConstraintViolationWithTemplate("First name is required for new customers.")
                .addPropertyNode("firstName").addConstraintViolation();
            valid = false;
        }
        if (isBlank(request.getEmail())) {
            context.buildConstraintViolationWithTemplate("Email is required for new customers.")
                .addPropertyNode("email").addConstraintViolation();
            valid = false;
        }
        if (isBlank(request.getPhoneNumber())) {
            context.buildConstraintViolationWithTemplate("Phone number is required for new customers.")
                .addPropertyNode("phoneNumber").addConstraintViolation();
            valid = false;
        }
        if (request.getAddress() == null) {
            context.buildConstraintViolationWithTemplate("Address is required for new customers.")
                .addPropertyNode("address").addConstraintViolation();
            valid = false;
        }
        return valid;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

