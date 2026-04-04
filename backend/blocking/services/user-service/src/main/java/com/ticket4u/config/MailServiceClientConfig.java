package com.ticket4u.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class MailServiceClientConfig {

    private final MailServiceProperties mailServiceProperties;

    @Bean
    public RestClient mailServiceRestClient() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(5));
        factory.setReadTimeout(Duration.ofSeconds(15));

        return RestClient.builder()
                .requestFactory(factory)
                .baseUrl(mailServiceProperties.getUrl())
                .defaultHeader("X-API-Key", mailServiceProperties.getApiKey())
                .build();
    }
}
