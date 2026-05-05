package com.ticket4u.mailservice.client;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import com.ticket4u.mailservice.config.MailConfig;
import com.ticket4u.mailservice.config.ResendClientConfig;
import com.ticket4u.mailservice.exception.EmailSendException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResendClient {

    private final Resend resend;
    private final MailConfig mailConfig;

    public void sendEmail(String to, String subject, String htmlContent, String[] cc, String[] bcc) {
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(String.format("%s <%s>", mailConfig.getFromName(), mailConfig.getFrom()))
                .to(to)
                .subject(subject)
                .html(htmlContent)
                .cc(filterValidEmails(cc))
                .bcc(filterValidEmails(bcc))
                .build();

        try {
            CreateEmailResponse response = resend.emails().send(params);
            log.info("Email sent successfully! ID: {}", response.getId());
        } catch (ResendException e) {
            log.error("Resend API failed to send email to {}: {}", to, e.getMessage());
            throw new EmailSendException("Resend error: " + e.getMessage(), e);
        }
    }

    private List<String> filterValidEmails(String[] emails) {
        if (emails == null) return Collections.emptyList();
        return Arrays.stream(emails)
                .filter(e -> e != null && !e.isBlank())
                .toList();
    }
}
