package com.ticket4u.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceClient {

    private final RestClient mailServiceRestClient;

    public void sendVerificationEmail(String to, String userName, String verificationLink, int expiryHours) {
        Map<String, Object> body = Map.of(
                "to", to,
                "subject", "Xác thực email - Ticket4U",
                "templateCode", "EMAIL_VERIFICATION",
                "templateData", Map.of(
                        "userName", userName,
                        "verificationLink", verificationLink,
                        "expiryHours", String.valueOf(expiryHours)
                ),
                "async", true
        );

        try {
            mailServiceRestClient.post()
                    .uri("/api/v1/mail/send")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();
            log.info("Verification email sent to {}", to);
        } catch (Exception e) {
            log.warn("Failed to send verification email to {}: {}", to, e.getMessage(), e);
        }
    }
}
