package com.ticket4u.jwk.supplier;

import com.nimbusds.jose.jwk.JWK;
import com.ticket4u.jwk.exception.JwkRetrievalException;

import java.util.Optional;

/**
 * Interface for supplying JWK Sets dynamically for JWT token verification.
 * Implementations can fetch JWK Sets from other services via internal routes and caffeine caching
 */
public interface JwkSupplier {

    /**
     * Retrieves the current JWK from set key identifier.
     *
     * @return the JWK Set, or empty if unavailable
     * @throws JwkRetrievalException if retrieval fails
     */
    JWK getJwk() throws JwkRetrievalException;

    /**
     * Retrieves the current JWK as an Optional.
     * This method doesn't throw exceptions and returns empty on failure.
     *
     * @return Optional containing the JWK Set if available
     */
    default Optional<JWK> getJwkSafe() {
        try {
            return Optional.ofNullable(getJwk());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Forces a refresh of the JWK Set from its source.
     *
     * @return true if refresh was successful, false otherwise
     */
    boolean refresh();

    /**
     * Checks if the JWK Set is currently cached/available.
     *
     * @return true if JWK Set is available without fetching
     */
    default boolean isAvailable() {
        return getJwkSafe().isPresent();
    }
}