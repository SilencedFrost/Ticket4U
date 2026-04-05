package com.ticket4u.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class TicketServiceClientConfig {

    private final TicketServiceProperties ticketServiceProperties;

    @Bean
    public RestClient ticketServiceRestClient() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(5));
        factory.setReadTimeout(Duration.ofSeconds(15));

        return RestClient.builder()
                .requestFactory(factory)
                .baseUrl(ticketServiceProperties.getUrl())
                .defaultHeader("X-API-KEY", ticketServiceProperties.getApiKey())
                .build();
    }
}
