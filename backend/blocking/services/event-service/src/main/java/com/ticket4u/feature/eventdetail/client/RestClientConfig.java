package com.ticket4u.feature.eventdetail.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.http.HttpClient;
import java.security.cert.X509Certificate;

@Configuration
public class RestClientConfig {
    @Value("${app.services.user-service.url}")
    private String userServiceUrl;

    @Bean
    public RestClient userServiceClient() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() { return null; }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpClient httpClient = HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .build();

            return RestClient.builder()
                    .baseUrl(userServiceUrl)
                    .requestFactory(new JdkClientHttpRequestFactory(httpClient))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Fail RestClient Config", e);
        }
    }
}
