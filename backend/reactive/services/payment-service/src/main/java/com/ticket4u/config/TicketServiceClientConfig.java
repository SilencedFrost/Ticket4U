package com.ticket4u.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.net.http.HttpClient;

@Configuration
@RequiredArgsConstructor
public class TicketServiceClientConfig {

    private final TicketServiceProperties ticketServiceProperties;

    @Bean
    public RestClient ticketServiceRestClient() {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(httpClient);
        factory.setReadTimeout(Duration.ofSeconds(15));

        return RestClient.builder()
                .requestFactory(factory)
                .baseUrl(ticketServiceProperties.getUrl())
                .defaultHeader("X-API-KEY", ticketServiceProperties.getApiKey())
                .build();
    }
}
