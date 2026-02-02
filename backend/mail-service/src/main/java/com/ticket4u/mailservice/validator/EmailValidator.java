package com.ticket4u.mailservice.validator;

import com.ticket4u.mailservice.dto.request.EmailRequest;
import com.ticket4u.mailservice.enums.TemplateType;
import com.ticket4u.mailservice.exception.TemplateNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class EmailValidator {

    public void validate(EmailRequest request) {
        TemplateType templateType = TemplateType.fromCode(request.getTemplateCode());
        if (templateType == null) {
            throw new TemplateNotFoundException(
                "Invalid template code: " + request.getTemplateCode() + 
                ". Valid codes: OTP, WELCOME, RESET_PASSWORD, NOTIFICATION, OTP_CUSTOM_A, OTP_CUSTOM_B, WELCOME_CUSTOM"
            );
        }
    }
}
