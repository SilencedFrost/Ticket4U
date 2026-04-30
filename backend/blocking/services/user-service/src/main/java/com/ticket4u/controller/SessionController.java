package com.ticket4u.controller;

import com.ticket4u.constant.TokenConstants;
import com.ticket4u.dto.session.SessionResponse;
import com.ticket4u.entity.CustomUserDetails;
import com.ticket4u.service.SessionService;
import com.ticket4u.util.AuthPrincipalUtil;
import com.ticket4u.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.DigestException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
@Slf4j
public class SessionController {
    private final SessionService sessionService;
    private final CookieUtil cookieUtil;

    /**
     * GET /api/v1/sessions
     * To get current logged in account's active sessions
     *
     * @param principal the authenticated user's details, injected by Spring Security
     * @return list of active sessions belonging to the current user
     */
    @GetMapping
    public ResponseEntity<List<SessionResponse>> getCurrentUserSessions(
            @AuthenticationPrincipal CustomUserDetails principal,
            HttpServletRequest request
    ) {
        UUID currentUserId = AuthPrincipalUtil.extractUserIdOrThrow(principal);

        //xác định current session bằng cách hash rt cookie
        String currentSessionHash = Optional.ofNullable(request.getCookies())
                .flatMap(cookies -> cookieUtil.getCookie(cookies, TokenConstants.REFRESH_TOKEN.getCookieKey()))
                .map(DigestUtils::sha256Hex)
                .orElse(null);
        return ResponseEntity.ok(sessionService.getSessionsByUserId(currentUserId, currentSessionHash));
    }

    /**
     * DELETE /api/v1/sessions/{display-id}
     * To delete a specific session using the first 6 characters of the hashed session id
     *
     * @param principal the authenticated user's details, injected by Spring Security
     * @param displayId 6-character string (case preserved)
     *                  
     * @return ResponseEntity<?> HTTP 204 No Content with empty body on success
     */
    @DeleteMapping("/{display-id}")
    public ResponseEntity<?> deleteSession(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable("display-id") String displayId
    ) {
        UUID currentUserId = AuthPrincipalUtil.extractUserIdOrThrow(principal);
        log.info("Request to delete session with hash prefix: {}", displayId);
        sessionService.deleteSessionByDisplayId(currentUserId, displayId);
        return ResponseEntity.noContent().build();
    }
}
