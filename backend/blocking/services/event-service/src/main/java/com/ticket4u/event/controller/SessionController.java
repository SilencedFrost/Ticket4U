package com.ticket4u.event.controller;

import com.ticket4u.core.dto.LayoutResponse;
import com.ticket4u.event.layout.service.LayoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/public/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final LayoutService layoutService;

    @GetMapping("/{session-id}/layout")
    public ResponseEntity<LayoutResponse> getLayout(@PathVariable(name = "session-id") UUID sessionId) {
        return ResponseEntity.ok(layoutService.getLayout(sessionId));
    }


}
