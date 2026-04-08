package com.ticket4u.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "application.payment")
public class PaymentProperties {
    private String webhookKey;
    private String orderCodePrefix = "T4U";
    private SepayProperties sepay = new SepayProperties();

    public String resolveOrderCodePrefix() {
        if (!StringUtils.hasText(orderCodePrefix)) {
            return "T4U";
        }

        return orderCodePrefix.trim();
    }

    @Getter
    @Setter
    public static class SepayProperties {
        private String bankCode;
        private String accountNumber;
        private String accountName;
        private String template = "compact";
    }
}
