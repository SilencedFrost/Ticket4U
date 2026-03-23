package com.ticket4u.core.controller;

import com.nimbusds.jose.jwk.JWK;
import com.ticket4u.core.entity.CustomUserDetails;
import com.ticket4u.jwk.supplier.EventJwkSupplier;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class HealthController {

    private final EventJwkSupplier eventJwkSupplier;

    @GetMapping
    public ResponseEntity<?> health() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/jwk/event")
    public ResponseEntity<Map<String, Object>> eventJwkHealth() {
        return eventJwkSupplier.getJwkSafe()
                .map(JWK::toJSONObject)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.internalServerError().build());
    }

    @GetMapping("/jwk/user")
    public ResponseEntity<CustomUserDetails> verifyUserWithJwk(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(userDetails);
    }
}