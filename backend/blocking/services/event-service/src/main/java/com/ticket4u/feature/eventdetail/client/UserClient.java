package com.ticket4u.feature.eventdetail.client;

import com.ticket4u.feature.eventdetail.dto.EventDetailResponse;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.http.HttpClient;
import java.security.cert.X509Certificate;
import java.util.UUID;

@Component
public class UserClient {
    private final RestClient restClient;

    public UserClient() {
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

            this.restClient = RestClient.builder()
                    .baseUrl("https://localhost:8080")
                    .requestFactory(new JdkClientHttpRequestFactory(httpClient))
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public EventDetailResponse.OrganizerDTO getOrganizerById(UUID id) {
        return restClient.get()
                .uri("/api/v1/organizers/{id}", id)
                .retrieve()
                .body(EventDetailResponse.OrganizerDTO.class);
    }
}
