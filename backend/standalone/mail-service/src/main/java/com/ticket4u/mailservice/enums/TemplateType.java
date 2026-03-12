package com.ticket4u.mailservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TemplateType {
    OTP("email/otp", "Xác thực OTP"),
    OTP_CUSTOM_A("email/custom/otp-custom-a", "OTP Custom A"),
    OTP_CUSTOM_B("email/custom/otp-custom-b", "OTP Custom B"),
    WELCOME("email/welcome", "Chào mừng"),
    WELCOME_CUSTOM("email/custom/welcome-custom", "Welcome Custom"),
    EMAIL_VERIFICATION("email/email-verification", "Xác thực email");


    private final String templatePath;
    private final String description;

    public static TemplateType fromCode(String code) {
        if (code == null) return null;
        try {
            return TemplateType.valueOf(code.toUpperCase().replace("-", "_"));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
