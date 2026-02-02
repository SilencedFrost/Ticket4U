package com.ticket4u.jwk.supplier;

import com.nimbusds.jose.jwk.JWKSet;
import com.ticket4u.jwk.exception.JwkSetRetrievalException;

public class EventJwkSetSupplier implements JwkSetSupplier{

    @Override
    public JWKSet getJwkSet() throws JwkSetRetrievalException {
        return null;
    }

    @Override
    public boolean refresh() {
        return false;
    }

    @Override
    public String getIdentifier() {
        return "";
    }
}
