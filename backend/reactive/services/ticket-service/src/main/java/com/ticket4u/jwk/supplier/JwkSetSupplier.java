package com.ticket4u.jwk.supplier;

import com.nimbusds.jose.jwk.JWKSet;
import java.util.Optional;
import com.ticket4u.jwk.exception.JwkSetRetrievalException;

/**
 * Interface for supplying JWK Sets dynamically for JWT token verification.
 * Implementations can fetch JWK Sets from other services via internal routes and caffeine caching
 */
public interface JwkSetSupplier {

    /**
     * Retrieves the current JWK Set.
     *
     * @return the JWK Set, or empty if unavailable
     * @throws JwkSetRetrievalException if retrieval fails
     */
    JWKSet getJwkSet() throws JwkSetRetrievalException;

    /**
     * Retrieves the current JWK Set as an Optional.
     * This method doesn't throw exceptions and returns empty on failure.
     *
     * @return Optional containing the JWK Set if available
     */
    default Optional<JWKSet> getJwkSetSafe() {
        try {
            return Optional.ofNullable(getJwkSet());
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
     * Gets the identifier for this JWK Set supplier (e.g., service name).
     * Useful when managing multiple suppliers.
     *
     * @return identifier string
     */
    String getIdentifier();

    /**
     * Checks if the JWK Set is currently cached/available.
     *
     * @return true if JWK Set is available without fetching
     */
    default boolean isAvailable() {
        return getJwkSetSafe().isPresent();
    }
}