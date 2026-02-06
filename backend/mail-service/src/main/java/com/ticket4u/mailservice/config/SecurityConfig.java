package com.ticket4u.mailservice.config;

import com.ticket4u.mailservice.exception.UnauthorizedException;
import com.ticket4u.mailservice.service.AuditService;
import com.ticket4u.mailservice.util.SecurityUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Configuration
public class SecurityConfig {

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "security")
    public static class ApiKeyProperties {
        private Set<String> apiKeys;
    }

    @Slf4j
    @Component
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public static class ApiKeyFilter extends OncePerRequestFilter {

        private static final String API_KEY_HEADER = "X-API-KEY";
        private final ApiKeyProperties properties;
        private final AuditService auditService;

        public ApiKeyFilter(ApiKeyProperties properties, AuditService auditService) {
            this.properties = properties;
            this.auditService = auditService;
        }

        @Override
        protected boolean shouldNotFilter(HttpServletRequest request) {
            String path = request.getRequestURI();
            return path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.equals("/actuator/health")
                || path.equals("/");
        }

        @Override
        protected void doFilterInternal(
                @NonNull HttpServletRequest request,
                @NonNull HttpServletResponse response,
                @NonNull FilterChain filterChain
        ) throws ServletException, IOException {

            String apiKey = request.getHeader(API_KEY_HEADER);

            if (apiKey == null || apiKey.isBlank()) {
                throw UnauthorizedException.missingApiKey();
            }

            if (properties.getApiKeys() == null || !properties.getApiKeys().contains(apiKey)) {
                String clientIp = SecurityUtils.getClientIp(request);
                log.warn("[AUDIT] Auth FAILED - Invalid API Key: {}, IP: {}, Path: {}", 
                        SecurityUtils.maskApiKey(apiKey), clientIp, request.getRequestURI());
                throw UnauthorizedException.invalidApiKey();
            }

            String clientIp = SecurityUtils.getClientIp(request);
            log.info("[AUDIT] Auth SUCCESS - API Key: {}, IP: {}, Path: {}", 
                    SecurityUtils.maskApiKey(apiKey), clientIp, request.getRequestURI());
            filterChain.doFilter(request, response);
        }
    }
}
