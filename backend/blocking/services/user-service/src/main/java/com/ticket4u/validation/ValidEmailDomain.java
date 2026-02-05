package com.ticket4u.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailDomainValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmailDomain {
    String message() default "auth.error.format.email";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
