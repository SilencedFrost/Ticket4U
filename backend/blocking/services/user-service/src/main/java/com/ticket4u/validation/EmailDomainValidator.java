package com.ticket4u.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class EmailDomainValidator implements ConstraintValidator<ValidEmailDomain, String> {

    private static final Pattern EMAIL_DOMAIN_PATTERN = Pattern.compile(ValidationPatterns.EMAIL_DOMAIN);

    @Override
    public void initialize(ValidEmailDomain constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isBlank()) {
            return true;
        }
        return EMAIL_DOMAIN_PATTERN.matcher(email).matches();
    }
}
