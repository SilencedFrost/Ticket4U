package com.controller;

import com.dto.user.UserResponse;
import com.dto.user.UserUpdateRequest;
import com.service.SessionService;
import com.service.UserService;
import com.util.SessionCookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final SessionService sessionService;
    private final SessionCookieUtil sessionCookieUtil;

    /**
     * GET /api/users/profile
     * @return user via token
     */
    @GetMapping("/account")
    public ResponseEntity<UserResponse> getProfile(HttpServletRequest request) {
        return sessionCookieUtil.getSessionKey(request)
                .flatMap(sessionService::findUserBySessionToken)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    /**
     * PUT /api/users/profile
     * Update user's info
     * @return updated user
     */
    @PutMapping("/account")
    public ResponseEntity<UserResponse> updateProfile(
            @Valid @RequestBody UserUpdateRequest userUpdateRequest,
            HttpServletRequest request) {
        return sessionCookieUtil.getSessionKey(request)
                .flatMap(sessionService::findUserBySessionToken)
                .filter(user -> Objects.equals(user.userId(), userUpdateRequest.userId()))
                .map(user -> userService.update(userUpdateRequest))
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
   }
}
