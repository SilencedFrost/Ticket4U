package com.ticket4u.mailservice.service;

import com.ticket4u.mailservice.dto.request.EmailRequest;
import com.ticket4u.mailservice.util.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditService {

    private final HttpServletRequest httpRequest;

    public void logEmailRequest(EmailRequest request) {
        String clientIp = SecurityUtils.getClientIp(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");
        String apiKey = SecurityUtils.maskApiKey(httpRequest.getHeader("X-API-KEY"));
        int ccCount = request.getCc() != null ? request.getCc().length : 0;
        int bccCount = request.getBcc() != null ? request.getBcc().length : 0;

        log.info("[AUDIT] Email request - IP: {}, API-Key: {}, User-Agent: {}, To: {}, Template: {}, Async: {}, CC: {}, BCC: {}",
                clientIp, apiKey, userAgent, SecurityUtils.maskEmail(request.getTo()),
                request.getTemplateCode(), request.isAsync(), ccCount, bccCount);
    }

    public void logRateLimitExceeded(String apiKey, String clientIp, long waitSeconds) {
        log.warn("[AUDIT] Rate limit exceeded - API Key: {}, IP: {}, Wait: {}s",
                SecurityUtils.maskApiKey(apiKey), clientIp, waitSeconds);
    }
}
