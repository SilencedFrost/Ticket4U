package com.ticket4u.jwk.supplier;

import com.nimbusds.jose.jwk.JWK;
import com.ticket4u.jwk.exception.JwkRetrievalException;
import org.springframework.stereotype.Component;

@Component
public class EventJwkSupplier implements JwkSupplier {

    @Override
    public JWK getJwk() throws JwkRetrievalException {
        return null;
    }

    @Override
    public boolean refresh() {
        return false;
    }
}
