package com.ticket4u.validation;

import com.ticket4u.constant.MailNormalization;
import com.ticket4u.util.EmailUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailDomainValidator implements ConstraintValidator<ValidEmailDomain, String> {

    @Override
    public void initialize(ValidEmailDomain constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        String domain = EmailUtil.getDomain(email, true);

        if(domain == null || domain.isBlank()) return false;

        return MailNormalization.ALLOWED_EMAIL_DOMAIN.contains(domain.toLowerCase());
    }
}
