package com.ticket4u.mailservice.health;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmtpHealthIndicator {

    private final JavaMailSenderImpl mailSender;

    public HealthStatus check() {
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("smtp_host", mailSender.getHost());
        details.put("smtp_port", mailSender.getPort());
        
        try {
            mailSender.testConnection();
            details.put("connection", "OK");
            return new HealthStatus("UP", details);
        } catch (MessagingException e) {
            log.error("SMTP health check failed: {}", e.getMessage());
            details.put("error", e.getMessage());
            return new HealthStatus("DOWN", details);
        }
    }

    public record HealthStatus(String status, Map<String, Object> details) {}
}
