package com.ticket4u.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "application.mail-service")
public class MailServiceProperties {
    private String url;
    private String apiKey;
}
