package com.ticket4u.controller;

import com.ticket4u.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/.well-known")
@RequiredArgsConstructor
@Slf4j
public class JwksController {

    private final JwtUtil jwtUtil;

    @GetMapping(value = "/jwks.json")
    public ResponseEntity<?> getJwks() {
        try {
            return ResponseEntity.ok(jwtUtil.getPublicJWK().toJSONObject());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
