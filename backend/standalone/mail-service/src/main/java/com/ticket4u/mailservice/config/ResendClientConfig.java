package com.ticket4u.mailservice.config;

import com.resend.Resend;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ResendClientConfig {

    @Value("${mail.resend.api-key}")
    private String apiKey;

    @Bean
    public Resend resend() {
        return new Resend(apiKey);
    }
}
