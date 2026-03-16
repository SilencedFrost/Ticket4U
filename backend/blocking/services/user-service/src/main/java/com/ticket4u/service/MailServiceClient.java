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
        sendMail(to, "Xác thực email - Ticket4U", "EMAIL_VERIFICATION", Map.of(
                "userName", userName,
                "verificationLink", verificationLink,
                "expiryHours", String.valueOf(expiryHours)
        ));
    }

    public void sendPasswordResetEmail(String to, String userName, String resetLink, int expiryHours) {
        sendMail(to, "Đặt lại mật khẩu - Ticket4U", "PASSWORD_RESET", Map.of(
                "userName", userName,
                "resetLink", resetLink,
                "expiryHours", String.valueOf(expiryHours)
        ));
    }

    private void sendMail(String to, String subject, String templateCode, Map<String, String> templateData) {
        Map<String, Object> body = Map.of(
                "to", to,
                "subject", subject,
                "templateCode", templateCode,
                "templateData", templateData,
                "async", true
        );

        try {
            mailServiceRestClient.post()
                    .uri("/api/v1/mail/send")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();
            log.info("{} email sent to {}", templateCode, to);
        } catch (Exception e) {
            log.error("Failed to send {} email to {}: {}", templateCode, to, e.getMessage(), e);
        }
    }
}
