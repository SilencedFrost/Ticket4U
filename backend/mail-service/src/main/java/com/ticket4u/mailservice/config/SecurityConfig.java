package com.ticket4u.mailservice.config;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "security")
    public static class ApiKeyProperties {
        private List<String> apiKeys;
    }

    @Slf4j
    @Component
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public static class ApiKeyFilter extends OncePerRequestFilter {

        private static final String API_KEY_HEADER = "X-API-KEY";
        private final ApiKeyProperties properties;

        public ApiKeyFilter(ApiKeyProperties properties) {
            this.properties = properties;
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
                sendError(response, "Missing API Key");
                return;
            }

            if (properties.getApiKeys() == null || !properties.getApiKeys().contains(apiKey)) {
                log.warn("Invalid API Key attempt: {}", maskKey(apiKey));
                sendError(response, "Invalid API Key");
                return;
            }

            filterChain.doFilter(request, response);
        }

        private void sendError(HttpServletResponse response, String message) throws IOException {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(
                    "{\"error\": \"Unauthorized\", \"message\": \"" + message + "\"}"
            );
        }

        private String maskKey(String key) {
            if (key == null || key.length() < 8) return "***";
            return key.substring(0, 4) + "..." + key.substring(key.length() - 4);
        }
    }
}
