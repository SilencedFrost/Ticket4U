package com.ticket4u.jwk.supplier;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.nimbusds.jose.jwk.JWKSet;
import com.ticket4u.jwk.exception.JwkSetRetrievalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.time.Duration;

public class AuthJwkSetSupplier implements JwkSetSupplier {

    private static final Logger logger = LoggerFactory.getLogger(AuthJwkSetSupplier.class);
    private static final String CACHE_KEY = "auth-jwkset";
    private static final String JWKS_URL = "https://localhost:8080/auth/.well-known/jwks.json";
    private static final String IDENTIFIER = "auth-service";

    private final Cache<String, JWKSet> cache;
    private final HttpClient httpClient;
    private final String jwksUrl;
    private final String cacheKey;

    public AuthJwkSetSupplier() {
        this(JWKS_URL, CACHE_KEY);
    }

    public AuthJwkSetSupplier(String jwksUrl, String cacheKey) {
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
    public JWKSet getJwkSet() throws JwkSetRetrievalException {
        // Try to get from cache
        JWKSet jwkSet = cache.getIfPresent(cacheKey);

        if (jwkSet != null) {
            logger.debug("JWKSet retrieved from cache for key: {}", cacheKey);
            return jwkSet;
        }

        // Cache miss - attempt refresh
        logger.debug("JWKSet not found in cache for key: {}, attempting refresh", cacheKey);
        boolean refreshed = refresh();

        if (refreshed) {
            jwkSet = cache.getIfPresent(cacheKey);
            if (jwkSet != null) {
                logger.info("JWKSet successfully retrieved after refresh for key: {}", cacheKey);
                return jwkSet;
            }
        }

        // Still not found after refresh
        logger.error("Failed to retrieve JWKSet after refresh attempt for key: {}", cacheKey);
        throw new JwkSetRetrievalException("Failed to retrieve JWKSet from " + jwksUrl);
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
                JWKSet jwkSet = JWKSet.parse(response.body());
                cache.put(cacheKey, jwkSet);
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

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }
}