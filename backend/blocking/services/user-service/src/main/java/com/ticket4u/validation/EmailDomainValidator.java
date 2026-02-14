package com.ticket4u.validation;

import com.ticket4u.constant.ValidationConstants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class EmailDomainValidator implements ConstraintValidator<ValidEmailDomain, String> {

    @Override
    public void initialize(ValidEmailDomain constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isBlank()) return false;

        // Validate the @ symbol
        int atIndex = email.indexOf('@');
        if (atIndex == -1 || atIndex == email.length() - 1) return false;

        // Validate domain
        String domain = email.substring(atIndex + 1);
        return ValidationConstants.ALLOWED_EMAIL_DOMAIN.contains(domain);
    }
}
