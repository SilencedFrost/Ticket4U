package com.ticket4u.controller;

import com.ticket4u.dto.user.ChangeEmailRequest;
import com.ticket4u.dto.user.ChangeInfoRequest;
import com.ticket4u.dto.user.ChangePasswordRequest;
import com.ticket4u.dto.user.UserSummaryResponse;
import com.ticket4u.entity.CustomUserDetails;
import com.ticket4u.service.EmailChangeService;
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
    private final EmailChangeService emailChangeService;

    /**
     * GET /api/v1/users
     * To get current logged in account info summary
     *
     * @param principal the authenticated user's details, injected by Spring Security
     * @return summary info of the current logged in user
     */
    @GetMapping
    public ResponseEntity<UserSummaryResponse> getCurrentUser(
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        UUID currentUserId = AuthPrincipalUtil.extractUserIdOrThrow(principal);
        return ResponseEntity.ok(userService.getSummaryByIdOrThrow(currentUserId));
    }

    /**
     * PATCH /api/v1/users
     * To modify account detail, not including password
     *
     * @param principal the authenticated user's details, injected by Spring Security
     * @param request   fields to update
     * @return updated summary info of the current logged in user
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
     *
     * @param principal the authenticated user's details, injected by Spring Security
     * @param request   contains current password for verification and the new password
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

    /**
     * PATCH /api/v1/users/email
     * Initiate email change — sends verification link to the new email address.
     * Always returns 202 to avoid enumerable attack.
     *
     * @param principal the authenticated user's details, injected by Spring Security
     * @param request   contains new email and current password for verification
     */
    @PatchMapping("/email")
    public ResponseEntity<Void> changeEmail(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestBody @Valid ChangeEmailRequest request
    ) {
        UUID currentUserId = AuthPrincipalUtil.extractUserIdOrThrow(principal);
        emailChangeService.initiateEmailChange(currentUserId, request);
        return ResponseEntity.accepted().build();
    }
}
