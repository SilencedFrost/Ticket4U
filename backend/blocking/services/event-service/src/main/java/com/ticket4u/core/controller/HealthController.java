package com.ticket4u.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: implement JWK and user auth status check
@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class HealthController {

    @GetMapping
    public ResponseEntity<?> health() {
        return ResponseEntity.ok().build();
    }
}
