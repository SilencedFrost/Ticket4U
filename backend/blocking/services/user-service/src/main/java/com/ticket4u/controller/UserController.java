package com.ticket4u.controller;

import com.ticket4u.dto.user.ChangeInfoRequest;
import com.ticket4u.dto.user.ChangePasswordRequest;
import com.ticket4u.dto.user.UserSummaryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    /**
     * GET /api/v1/users
     * To get current logged in account info summary
     * @return
     */
    @GetMapping
    public ResponseEntity<UserSummaryResponse> getCurrentUser() {
        return null;
    }

    /**
     * PATCH api/v1/users
     * To modify account detail, not include password
     * @param request
     * @return
     */
    @PatchMapping
    public ResponseEntity<UserSummaryResponse> updateCurrentUser(@RequestBody @Valid ChangeInfoRequest request) {
        return null;
    }

    /**
     * PATCH /api/v1/users/password
     * To modify account password
     * @param request
     * @return
     */
    @PatchMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        return null;
    }
}
