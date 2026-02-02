package com.ticket4u.mailservice.service;

import com.ticket4u.mailservice.dto.request.EmailRequest;
import com.ticket4u.mailservice.dto.response.EmailResponse;
import com.ticket4u.mailservice.enums.TemplateType;
import com.ticket4u.mailservice.processor.TemplateProcessor;
import com.ticket4u.mailservice.validator.EmailValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final BrevoClientService brevoClientService;
    private final TemplateProcessor templateProcessor;
    private final EmailValidator emailValidator;

    @Override
    public EmailResponse send(EmailRequest request) {
        emailValidator.validate(request);
        String messageId = UUID.randomUUID().toString();

        if (request.isAsync()) {
            sendAsync(request, messageId);
            return EmailResponse.queued(messageId);
        }
        return sendSync(request);
    }

    @Override
    public EmailResponse sendSync(EmailRequest request) {
        String messageId = UUID.randomUUID().toString();
        TemplateType templateType = TemplateType.fromCode(request.getTemplateCode());
        String htmlContent = templateProcessor.process(templateType, request.getTemplateData());

        log.info("Sending email sync to: {}", request.getTo());
        brevoClientService.sendEmail(
                request.getTo(),
                request.getSubject(),
                htmlContent,
                request.getCc(),
                request.getBcc()
        );

        return EmailResponse.sent(messageId);
    }

    @Async("emailExecutor")
    @Override
    public void sendAsync(EmailRequest request, String messageId) {
        try {
            TemplateType templateType = TemplateType.fromCode(request.getTemplateCode());
            String htmlContent = templateProcessor.process(templateType, request.getTemplateData());

            log.info("Sending email async [{}] to: {}", messageId, request.getTo());
            brevoClientService.sendEmail(
                    request.getTo(),
                    request.getSubject(),
                    htmlContent,
                    request.getCc(),
                    request.getBcc()
            );
            log.info("Email sent async [{}]", messageId);
        } catch (Exception e) {
            log.error("Failed to send async email [{}]: {}", messageId, e.getMessage());
        }
    }
}
