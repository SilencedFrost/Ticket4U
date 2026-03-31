package com.ticket4u.controller;

import com.ticket4u.dto.user.ChangeInfoRequest;
import com.ticket4u.dto.user.ChangePasswordRequest;
import com.ticket4u.dto.user.UserSummaryResponse;
import com.ticket4u.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
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
            @AuthenticationPrincipal UUID currentUserId
    ) {
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
            @AuthenticationPrincipal UUID currentUserId,
            @RequestBody @Valid ChangeInfoRequest request
    ) {
        return ResponseEntity.ok(userService.updateInfo(currentUserId, request));
    }

    /**
     * PATCH /api/v1/users/password
     * To modify account password
     * @param request
     * @return
     */
    @PatchMapping("/password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal UUID currentUserId,
            @RequestBody @Valid ChangePasswordRequest request
    ) {
        userService.changePassword(currentUserId, request);
        return ResponseEntity.ok().build();
    }
}
