package com.ticket4u.controller;

import com.ticket4u.dto.user.ChangeInfoRequest;
import com.ticket4u.dto.user.ChangePasswordRequest;
import com.ticket4u.dto.user.UserSummaryResponse;
import com.ticket4u.entity.CustomUserDetails;
import com.ticket4u.service.UserService;
import com.ticket4u.util.AuthPrincipalUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * GET /api/v1/users
     * To get current logged in account info summary
     * @return
     */
    @GetMapping
    public ResponseEntity<UserSummaryResponse> getCurrentUser(
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        UUID currentUserId = AuthPrincipalUtil.extractUserIdOrThrow(principal);
        return ResponseEntity.ok(userService.getSummaryByIdOrThrow(currentUserId));
    }

    /**
     * PATCH api/v1/users
     * To modify account detail, not include password
     * @param request
     * @return
     */
    @PatchMapping
    public ResponseEntity<UserSummaryResponse> updateCurrentUser(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestBody @Valid ChangeInfoRequest request
    ) {
        UUID currentUserId = AuthPrincipalUtil.extractUserIdOrThrow(principal);
        return ResponseEntity.ok(userService.updateEntity(currentUserId, request));
    }

    /**
     * PATCH /api/v1/users/password
     * To modify account password
     * @param request
     * @return
     */
    @PatchMapping("/password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestBody @Valid ChangePasswordRequest request
    ) {
        UUID currentUserId = AuthPrincipalUtil.extractUserIdOrThrow(principal);
        userService.changePassword(currentUserId, request);
        return ResponseEntity.ok().build();
    }
}
