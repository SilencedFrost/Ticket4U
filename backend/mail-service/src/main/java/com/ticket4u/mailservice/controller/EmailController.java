package com.ticket4u.mailservice.controller;

import com.ticket4u.mailservice.dto.request.EmailRequest;
import com.ticket4u.mailservice.dto.response.EmailResponse;
import com.ticket4u.mailservice.enums.TemplateType;
import com.ticket4u.mailservice.service.EmailService;
import com.ticket4u.mailservice.health.SmtpHealthIndicator;
import com.ticket4u.mailservice.health.SmtpHealthIndicator.HealthStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
@Tag(name = "Email API", description = "API gửi email qua Brevo SMTP")
public class EmailController {

    private final EmailService emailService;
    private final HttpServletRequest httpRequest;
    private final SmtpHealthIndicator smtpHealthIndicator;

    @PostMapping("/send")
    @Operation(
        summary = "Gửi email",
        description = "Gửi email với template. Sử dụng async=true để gửi không đồng bộ"
    )
    public ResponseEntity<EmailResponse> sendEmail(
            @Valid @RequestBody EmailRequest request
    ) {
        logRequest(request);
        EmailResponse response = emailService.send(request);
        log.info("[AUDIT] Response: messageId={}, status={}", response.getMessageId(), response.getStatus());
        return ResponseEntity.ok(response);
    }

    private void logRequest(EmailRequest request) {
        String clientIp = getClientIp();
        String userAgent = httpRequest.getHeader("User-Agent");
        String apiKey = maskApiKey(httpRequest.getHeader("X-API-KEY"));
        int ccCount = request.getCc() != null ? request.getCc().length : 0;
        int bccCount = request.getBcc() != null ? request.getBcc().length : 0;

        log.info("[AUDIT] Email request - IP: {}, API-Key: {}, User-Agent: {}, To: {}, Template: {}, Async: {}, CC: {}, BCC: {}",
                clientIp, apiKey, userAgent, maskEmail(request.getTo()), 
                request.getTemplateCode(), request.isAsync(), ccCount, bccCount);
    }

    private String getClientIp() {
        String xff = httpRequest.getHeader("X-Forwarded-For");
        return xff != null ? xff.split(",")[0].trim() : httpRequest.getRemoteAddr();
    }

    private String maskApiKey(String key) {
        if (key == null || key.length() < 8) return "***";
        return key.substring(0, 8) + "...";
    }

    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) return "***";
        int atIdx = email.indexOf("@");
        return email.substring(0, Math.min(3, atIdx)) + "***@" + email.substring(atIdx + 1);
    }

    @GetMapping("/templates")
    @Operation(summary = "Danh sách template codes")
    public ResponseEntity<List<Map<String, String>>> getTemplates() {
        List<Map<String, String>> templates = Arrays.stream(TemplateType.values())
                .map(t -> Map.of(
                        "code", t.name(),
                        "description", t.getDescription(),
                        "path", t.getTemplatePath()
                ))
                .toList();
        return ResponseEntity.ok(templates);
    }

    @GetMapping("/health")
    @Operation(summary = "Health check với SMTP connection test")
    public ResponseEntity<HealthStatus> health() {
        HealthStatus health = smtpHealthIndicator.check();
        if (health.status().equals("UP")) {
            return ResponseEntity.ok(health);
        }
        return ResponseEntity.status(503).body(health);
    }
}
