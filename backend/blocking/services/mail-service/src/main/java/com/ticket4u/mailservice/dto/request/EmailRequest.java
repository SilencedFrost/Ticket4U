package com.ticket4u.mailservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Email request payload")
public class EmailRequest {

    @NotBlank(message = "Email người nhận không được trống")
    @Email(message = "Email không hợp lệ")
    @Schema(description = "Email người nhận", example = "user@example.com")
    private String to;

    @NotBlank(message = "Tiêu đề không được trống")
    @Schema(description = "Tiêu đề email", example = "Xác thực OTP")
    private String subject;

    @NotNull(message = "Template code không được trống")
    @Schema(description = "Mã template: OTP, WELCOME, RESET_PASSWORD, NOTIFICATION", example = "OTP")
    private String templateCode;

    @Schema(description = "Dữ liệu để render template", example = "{\"otp\": \"123456\", \"userName\": \"John\"}")
    private Map<String, Object> templateData;

    @Builder.Default
    @Schema(description = "true = gửi async (không chờ), false = gửi sync", example = "false")
    private boolean async = false;

    @Schema(description = "CC emails (tùy chọn)", example = "[]")
    private String[] cc;

    @Schema(description = "BCC emails (tùy chọn)", example = "[]")
    private String[] bcc;
}
