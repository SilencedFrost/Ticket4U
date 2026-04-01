package com.ticket4u.controller;

import com.ticket4u.dto.session.SessionResponse;
import com.ticket4u.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
@Slf4j
public class SessionController {
    private final SessionService sessionService;
    /**
     * GET /api/v1/sessions
     * To get current logged in account's active sessions
     * @return List of SessionResponse
     */
    @GetMapping
    public ResponseEntity<List<SessionResponse>> getCurrentUserSessions(
            @AuthenticationPrincipal UUID currentUserId
    ) {
        return ResponseEntity.ok(sessionService.getSessionsByUserId(currentUserId));
    }

    /**
     * DELETE /api/v1/sessions/{display-id}
     * To delete a specific session using the first 6 characters of the hashed session id
     * @param displayId 6-character string (case preserved)
     * @return ResponseEntity<?> HTTP 200 OK with empty body on success
     */
    @DeleteMapping("/{display-id}")
    public ResponseEntity<?> deleteSession(
            @AuthenticationPrincipal UUID currentUserId,
            @PathVariable String displayId
    ) {
        log.info("Request to delete session with hash prefix: {}", displayId);
        sessionService.deleteSessionByDisplayId(currentUserId, displayId);
        return ResponseEntity.ok().build();
    }
}
