package com.ticket4u.mailservice.controller;

import com.ticket4u.mailservice.dto.request.EmailRequest;
import com.ticket4u.mailservice.dto.response.EmailResponse;
import com.ticket4u.mailservice.enums.TemplateType;
import com.ticket4u.mailservice.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
@Tag(name = "Email API", description = "API gửi email qua Brevo SMTP")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    @Operation(
        summary = "Gửi email",
        description = "Gửi email với template. Sử dụng async=true để gửi không đồng bộ"
    )
    public ResponseEntity<EmailResponse> sendEmail(
            @Valid @RequestBody EmailRequest request
    ) {
        EmailResponse response = emailService.send(request);
        return ResponseEntity.ok(response);
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
    @Operation(summary = "Health check")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }
}
