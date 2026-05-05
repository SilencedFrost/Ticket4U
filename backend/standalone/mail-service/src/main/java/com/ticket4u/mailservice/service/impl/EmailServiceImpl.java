package com.ticket4u.mailservice.service.impl;

import com.ticket4u.mailservice.client.ResendClient;
import com.ticket4u.mailservice.dto.request.EmailRequest;
import com.ticket4u.mailservice.dto.response.EmailResponse;
import com.ticket4u.mailservice.enums.TemplateType;
import com.ticket4u.mailservice.processor.TemplateProcessor;
import com.ticket4u.mailservice.service.EmailService;
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

    private final ResendClient resendClient;
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
        long startTime = System.currentTimeMillis();
        
        TemplateType templateType = TemplateType.fromCode(request.getTemplateCode());
        log.info("[AUDIT] Email PROCESSING - messageId: {}, template: {}, to: {}", 
                messageId, templateType.name(), request.getTo());
        
        String htmlContent = templateProcessor.process(templateType, request.getTemplateData());

        resendClient.sendEmail(
                request.getTo(),
                request.getSubject(),
                htmlContent,
                request.getCc(),
                request.getBcc()
        );

        long duration = System.currentTimeMillis() - startTime;
        log.info("[AUDIT] Email SENT - messageId: {}, duration: {}ms", messageId, duration);
        return EmailResponse.sent(messageId);
    }

    @Async("emailExecutor")
    @Override
    public void sendAsync(EmailRequest request, String messageId) {
        long startTime = System.currentTimeMillis();
        TemplateType templateType = null;
        try {
            templateType = TemplateType.fromCode(request.getTemplateCode());
            log.info("[AUDIT] Email ASYNC PROCESSING - messageId: {}, template: {}, to: {}", 
                    messageId, templateType.name(), request.getTo());
            
            String htmlContent = templateProcessor.process(templateType, request.getTemplateData());
            resendClient.sendEmail(
                    request.getTo(),
                    request.getSubject(),
                    htmlContent,
                    request.getCc(),
                    request.getBcc()
            );
            
            long duration = System.currentTimeMillis() - startTime;
            log.info("[AUDIT] Email ASYNC SENT - messageId: {}, duration: {}ms", messageId, duration);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("[AUDIT] Email ASYNC FAILED - messageId: {}, template: {}, to: {}, duration: {}ms, error: {}", 
                    messageId, templateType != null ? templateType.name() : "UNKNOWN", 
                    request.getTo(), duration, e.getMessage());
        }
    }
}
