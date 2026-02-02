package com.ticket4u.mailservice.dto.response;

import com.ticket4u.mailservice.enums.EmailStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Email response")
public class EmailResponse {

    @Schema(description = "ID tin nhắn")
    private String messageId;

    @Schema(description = "Trạng thái: PENDING, QUEUED, SENT, FAILED")
    private EmailStatus status;

    @Schema(description = "Thông báo")
    private String message;

    @Schema(description = "Thời gian")
    private Instant timestamp;

    public static EmailResponse queued(String messageId) {
        return EmailResponse.builder()
                .messageId(messageId)
                .status(EmailStatus.QUEUED)
                .message("Email đã được đưa vào hàng đợi")
                .timestamp(Instant.now())
                .build();
    }

    public static EmailResponse sent(String messageId) {
        return EmailResponse.builder()
                .messageId(messageId)
                .status(EmailStatus.SENT)
                .message("Email đã được gửi thành công")
                .timestamp(Instant.now())
                .build();
    }

    public static EmailResponse failed(String error) {
        return EmailResponse.builder()
                .status(EmailStatus.FAILED)
                .message(error)
                .timestamp(Instant.now())
                .build();
    }
}
