package com.ticket4u.jwk.supplier;

import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.util.IOUtils;
import com.ticket4u.jwk.exception.JwkRetrievalException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class EventJwkSupplier implements JwkSupplier {

    @Value("${application.security.jwt.public-key-path}")
    private String publicKeyPath;

    @Value("${application.security.jwt.key-id}")
    private String keyId;

    private JWK cachedKey;

    @PostConstruct
    public void init() throws Exception {
        String pem = IOUtils.readInputStreamToString(
                Objects.requireNonNull(
                        getClass().getClassLoader().getResourceAsStream(publicKeyPath),
                        "Public key not found at: " + publicKeyPath
                )
        );
        ECKey parsed = ECKey.parseFromPEMEncodedObjects(pem).toECKey();
        this.cachedKey = new ECKey.Builder(parsed).keyID(keyId).build();
        log.info("EventJwkSupplier initialized with key: {}", keyId);
    }

    @Override
    public JWK getJwk() throws JwkRetrievalException {
        if (cachedKey == null)
            throw new JwkRetrievalException("Event JWK not initialized");
        return cachedKey;
    }

    @Override
    public boolean refresh() {
        // Local PEM — no remote refresh needed
        // When JwkService is ready, implement remote fetch here
        return cachedKey != null;
    }
}