package com.ticket4u.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QdrantConfig {

    @Value("${qdrant.host}")
    private String host;

    @Value("${qdrant.port}")
    private int port;

    @Value("${qdrant.api-key:}") // API key, empty default for local dev
    private String apiKey;

    @Value("${qdrant.timeout-seconds:5}") // default timeout
    private long timeoutSeconds;

    @Bean
    public ManagedChannel qdrantChannel() {
        return ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext() // swap to .useTransportSecurity() when TLS is needed
                .build();
    }

    @Bean
    public QdrantClient qdrantClient(ManagedChannel qdrantChannel) {
        QdrantGrpcClient.Builder builder = QdrantGrpcClient.newBuilder(qdrantChannel, true); // true = client owns channel lifecycle
        if (apiKey != null && !apiKey.isBlank()) {
            builder.withApiKey(apiKey);
        }
        return new QdrantClient(builder.build());
    }

    // Spring will call this on context close → closes client → closes channel
    @Bean
    public DisposableBean qdrantClientDestroyer(QdrantClient qdrantClient) {
        return qdrantClient::close;
    }
}
