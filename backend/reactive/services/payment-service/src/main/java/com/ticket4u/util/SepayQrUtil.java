package com.ticket4u.util;

import com.ticket4u.config.PaymentProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class SepayQrUtil {

    private final PaymentProperties paymentProperties;

    public SepayQrDetails build(BigDecimal amount, String orderCode) {
        PaymentProperties.SepayProperties sepay = paymentProperties.getSepay();
        validateConfig(sepay);

        String amountValue = amount.stripTrailingZeros().toPlainString();
        String qrUrl = UriComponentsBuilder.fromUriString("https://qr.sepay.vn/img")
                .queryParam("acc", sepay.getAccountNumber())
                .queryParam("bank", sepay.getBankCode())
                .queryParam("amount", amountValue)
                .queryParam("des", orderCode)
                .queryParam("template", sepay.getTemplate())
                .build()
                .toUriString();

        return new SepayQrDetails(
                sepay.getBankCode(),
                sepay.getAccountNumber(),
                sepay.getAccountName(),
                sepay.getTemplate(),
                qrUrl);
    }

    private void validateConfig(PaymentProperties.SepayProperties sepay) {
        if (!StringUtils.hasText(sepay.getAccountNumber())) {
            throw new IllegalStateException("Missing SEPAY_QR_ACCOUNT_NUMBER configuration");
        }

        if (!StringUtils.hasText(sepay.getBankCode())) {
            throw new IllegalStateException("Missing SEPAY_QR_BANK_CODE configuration");
        }
    }

    public record SepayQrDetails(
            String bankCode,
            String accountNumber,
            String accountName,
            String template,
            String qrUrl) {
    }
}
