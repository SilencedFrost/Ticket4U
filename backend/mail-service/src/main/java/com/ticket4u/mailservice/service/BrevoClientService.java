package com.ticket4u.mailservice.service;

import com.ticket4u.mailservice.config.BrevoConfig;
import com.ticket4u.mailservice.exception.EmailSendException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrevoClientService {

    private final JavaMailSender mailSender;
    private final BrevoConfig brevoConfig;

    public void sendEmail(String to, String subject, String htmlContent, String[] cc, String[] bcc) {
        try {
            log.debug("Preparing email - To: {}, Subject: {}, CC: {}, BCC: {}", to, subject, cc, bcc);
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(brevoConfig.getFrom());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            if (cc != null && cc.length > 0 && hasValidEmails(cc)) {
                helper.setCc(filterValidEmails(cc));
            }
            if (bcc != null && bcc.length > 0 && hasValidEmails(bcc)) {
                helper.setBcc(filterValidEmails(bcc));
            }

            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);

        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
            throw new EmailSendException("Failed to send email: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error sending email to {}: {}", to, e.getMessage());
            throw new EmailSendException("Unexpected error: " + e.getMessage(), e);
        }
    }

    private boolean hasValidEmails(String[] emails) {
        if (emails == null) return false;
        for (String email : emails) {
            if (email != null && !email.isBlank()) return true;
        }
        return false;
    }

    private String[] filterValidEmails(String[] emails) {
        return java.util.Arrays.stream(emails)
                .filter(e -> e != null && !e.isBlank())
                .toArray(String[]::new);
    }
}
