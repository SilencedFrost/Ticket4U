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
        sendVerificationEmail(to, userName, verificationLink, expiryHours, true);
    }

    public void sendVerificationEmail(String to, String userName, String verificationLink, int expiryHours, boolean async) {
        sendMail(to, "Xác thực email - Ticket4U", "EMAIL_VERIFICATION", Map.of(
                "userName", userName,
            "title", "Xác thực email - Ticket4U",
            "introText", "Cảm ơn bạn đã đăng ký tài khoản tại Ticket4U.",
            "actionText", "Xác thực email",
            "actionLink", verificationLink,
            "expiryHours", String.valueOf(expiryHours),
            "expiryLabel", "Link xác thực sẽ hết hạn sau",
            "ignoreText", "Nếu bạn không đăng ký tài khoản này, vui lòng bỏ qua email này."
        ), async);
    }

    public void sendPasswordResetEmail(String to, String userName, String resetLink, int expiryHours) {
        sendMail(to, "Đặt lại mật khẩu - Ticket4U", "PASSWORD_RESET", Map.of(
                "userName", userName,
            "title", "Đặt lại mật khẩu - Ticket4U",
            "introText", "Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản Ticket4U của bạn.",
            "actionText", "Đặt lại mật khẩu",
            "actionLink", resetLink,
            "expiryHours", String.valueOf(expiryHours),
            "expiryLabel", "Link đặt lại mật khẩu sẽ hết hạn sau",
            "ignoreText", "Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này."
        ), true);
    }

    private void sendMail(String to, String subject, String templateCode, Map<String, String> templateData, boolean async) {
        Map<String, Object> body = Map.of(
                "to", to,
                "subject", subject,
                "templateCode", templateCode,
                "templateData", templateData,
                "async", async
        );

        mailServiceRestClient.post()
                .uri("/api/v1/mail/send")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();
        log.info("{} email sent to {}", templateCode, to);
    }
}
