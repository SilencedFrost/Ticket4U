package com.ticket4u.util;

import com.ticket4u.config.PaymentProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class PaymentWebhookAuthorizationUtil {

    private final PaymentProperties paymentProperties;

    public boolean isAuthorizationValid(String authorizationHeader) {
        String webhookKey = paymentProperties.getWebhookKey();
        if (!StringUtils.hasText(webhookKey)) {
            return true;
        }

        if (!StringUtils.hasText(authorizationHeader)) {
            return false;
        }

        String value = authorizationHeader.trim();
        if (!value.regionMatches(true, 0, "Apikey ", 0, 7)) {
            return false;
        }

        String receivedKey = value.substring(7).trim();
        return webhookKey.equals(receivedKey);
    }
}
