package com.ticket4u.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "application.verification")
public class VerificationProperties {
    private int tokenExpiryHours = 24;
    private int resetTokenExpiryHours = 1;
    private String frontendBaseUrl;
}
