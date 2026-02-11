package com.ticket4u.controller;

import com.nimbusds.jose.jwk.JWK;
import com.ticket4u.jwk.supplier.AuthJwkSupplier;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class HealthController {

    private final AuthJwkSupplier authJwkSupplier;

    @GetMapping
    public ResponseEntity<?> health() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/jwk/auth")
    public ResponseEntity<?> authJwkHealth() {
        return authJwkSupplier.getJwkSafe()
                .map(JWK::toJSONObject)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.internalServerError().build());
    }

    @GetMapping("/jwk/user")
    public ResponseEntity<?> verifyUserWithJwk() {
        return ResponseEntity.ok(Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal());
    }
}
