package com.harts.bank.api.request.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

//not being used, as per the current requirements, but can be used in future if we want to validate the new customer
// details when creating a savings account for a new customer
@Documented
@Constraint(validatedBy = com.harts.bank.api.request.validation.ValidateNewCustomerFieldsValidator.class)
@Target({ TYPE })
@Retention(RUNTIME)
public @interface ValidateNewCustomerFields {
    String message() default "Missing required customer details for new customer.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
