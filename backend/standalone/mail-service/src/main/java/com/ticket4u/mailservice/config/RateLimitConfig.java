package com.ticket4u.mailservice.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "rate-limit")
public class RateLimitConfig {

    private int requestsPerMinute = 60;
    private int requestsPerHour = 500;
    
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String apiKey) {
        return buckets.computeIfAbsent(apiKey, this::createBucket);
    }

    private Bucket createBucket(String apiKey) {
        Bandwidth perMinute = Bandwidth.builder()
                .capacity(requestsPerMinute)
                .refillGreedy(requestsPerMinute, Duration.ofMinutes(1))
                .build();
        Bandwidth perHour = Bandwidth.builder()
                .capacity(requestsPerHour)
                .refillGreedy(requestsPerHour, Duration.ofHours(1))
                .build();
        
        log.info("Created rate limit bucket for API key: {}... ({}/min, {}/hour)",
                apiKey.substring(0, Math.min(8, apiKey.length())),
                requestsPerMinute, requestsPerHour);
        
        return Bucket.builder()
                .addLimit(perMinute)
                .addLimit(perHour)
                .build();
    }

    public void clearBuckets() {
        buckets.clear();
    }
}
