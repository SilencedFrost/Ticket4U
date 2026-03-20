package com.ticket4u.jwk.supplier;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.ticket4u.jwk.exception.JwkRetrievalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.time.Duration;

@Component
public class AuthJwkSupplier implements JwkSupplier {

    private static final Logger logger = LoggerFactory.getLogger(AuthJwkSupplier.class);
    private static final String CACHE_KEY = "auth-jwkset";
    private static final String JWKS_URL = "https://localhost:8080/auth/.well-known/jwks.json";
    public static final String KEY_IDENTIFIER = "auth-key-es256";

    private final Cache<String, JWK> cache;
    private final HttpClient httpClient;
    private final String jwksUrl;
    private final String cacheKey;

    public AuthJwkSupplier() {
        this(JWKS_URL, CACHE_KEY);
    }

    public AuthJwkSupplier(String jwksUrl, String cacheKey) {
        this.jwksUrl = jwksUrl;
        this.cacheKey = cacheKey;
        this.cache = Caffeine.newBuilder()
                .maximumSize(1)
                .expireAfterWrite(Duration.ofHours(1))
                .build();

        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Override
    public JWK getJwk() throws JwkRetrievalException {
        // Try to get from cache
        JWK jwk = cache.getIfPresent(cacheKey);

        if (jwk != null) {
            logger.debug("JWKSet retrieved from cache for key: {}", cacheKey);
            return jwk;
        }

        // Cache miss - attempt refresh
        logger.debug("JWKSet not found in cache for key: {}, attempting refresh", cacheKey);
        boolean refreshed = refresh();

        if (refreshed) {
            jwk = cache.getIfPresent(cacheKey);
            if (jwk != null) {
                logger.info("JWKSet successfully retrieved after refresh for key: {}", cacheKey);
                return jwk;
            }
        }

        // Still not found after refresh
        logger.error("Failed to retrieve JWKSet after refresh attempt for key: {}", cacheKey);
        throw new JwkRetrievalException("Failed to retrieve JWKSet from " + jwksUrl);
    }

    @Override
    public boolean refresh() {
        try {
            logger.info("Fetching JWKSet from {}", jwksUrl);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(jwksUrl))
                    .GET()
                    .timeout(Duration.ofSeconds(10))
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Parse the JSON response which contains a single JWK as a JSON object
                JWK jwk = JWKSet.parse(response.body()).getKeyByKeyId(KEY_IDENTIFIER);
                cache.put(cacheKey, jwk);
                logger.info("JWKSet successfully fetched and cached with key: {}", cacheKey);
                return true;
            } else {
                logger.error("Failed to fetch JWKSet. HTTP status: {}", response.statusCode());
                return false;
            }

        } catch (IOException e) {
            logger.error("IO error while fetching JWKSet from {}", jwksUrl, e);
            return false;
        } catch (InterruptedException e) {
            logger.error("Request interrupted while fetching JWKSet from {}", jwksUrl, e);
            Thread.currentThread().interrupt();
            return false;
        } catch (ParseException e) {
            logger.error("Failed to parse JWKSet response from {}", jwksUrl, e);
            return false;
        }
    }
}