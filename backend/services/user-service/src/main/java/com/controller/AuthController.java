package com.controller;

import com.dto.auth.*;
import com.dto.user.UserResponse;
import com.exception.InvalidLoginException;
import com.service.EmailService;
import com.service.SessionService;
import com.service.UserService;
import com.util.SessionCookieUtil;
import com.util.TokenGeneratorUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;
    private final SessionService sessionService;
    private final SessionCookieUtil sessionCookieUtil;
    private final TokenGeneratorUtil tokenGeneratorUtil;
    private final EmailService emailService;

    /**
     * POST /api/auth/login
     * Validate user login and return the user DTO
     * @return User
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest loginRequest, @RequestHeader("User-Agent") String userAgent) {
        UserResponse userResponse = userService.authenticate(loginRequest).orElseThrow(() -> new InvalidLoginException("Incorrect login credentials"));

        if (loginRequest.rememberMe()) {
            String ua = userAgent != null ? userAgent : "Unknown";
            String sessionKey = sessionService.createSession(userResponse.userId(), ua);
            ResponseCookie sessionCookie = sessionCookieUtil.createSessionCookie(sessionKey);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, sessionCookie.toString())
                    .body(userResponse);
        }

        return ResponseEntity.ok().body(userResponse);
    }

    /**
     * GET /api/auth/login/session
     * @return user via token
     */
    @GetMapping("/login/session")
    public ResponseEntity<UserResponse> loginSession(HttpServletRequest request) {
        return sessionCookieUtil.getSessionKey(request)
                .flatMap(sessionService::findUserBySessionToken)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    /**
     * POST /api/auth/register/customer
     * @return Customer
     */
    @PostMapping("/register/customer")
    public ResponseEntity<String> registerCustomer(@Valid @RequestBody RegisterRequest registerRequest) {
        userService.registerIfNotExist(registerRequest);

        return ResponseEntity.ok("Registration successful. Please check your email to activate your account.");
    }

    @GetMapping("/verify/{token}")
    public ResponseEntity<Void> verifyAccount(@PathVariable String token) {
        boolean isVerified = userService.verifyToken(token);


        String frontendLoginUrl = "http://localhost:5173/auth/login";

        if (isVerified) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(frontendLoginUrl + "?verified=true"))
                    .build();
        } else {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(frontendLoginUrl + "?error=invalid_token"))
                    .build();
        }
    }

    /**
     * POST /api/auth/logout
     * Get session cookie, invalidate and clear cookie
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest httpServletRequest) {
        Optional<String> sessionToken = sessionCookieUtil.getSessionKey(httpServletRequest);

        if (sessionToken.isPresent()) {
            sessionService.invalidate(sessionToken.get());

            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, sessionCookieUtil.createDeleteCookie().toString()).build();
        }

        return ResponseEntity.noContent().build();
    }
    /**
     * POST /api/auth/forgot-password
     *
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody EmailRequest emailRequest,
                                                 HttpServletRequest request) {
        String email = emailRequest.email();

        boolean exists = userService.existsByEmail(email);
        if (!exists) {
            return ResponseEntity.badRequest().body("Invalid or unregistered email");
        }

        String otp = tokenGeneratorUtil.generateOtp();

        request.getSession(true).setAttribute("otp", otp);
        request.getSession().setAttribute("otpEmail", email);
        request.getSession().setMaxInactiveInterval(300);

        emailService.sendOtpEmail(email, otp);

        return ResponseEntity.ok("An OTP has been sent to  " + email);
    }

    /**
     * POST /api/auth/verify-otp
     *
     */

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(
            @Valid @RequestBody OtpRequest otpRequest,
            HttpServletRequest request) {

        String otpInput = otpRequest.otp();

        Object otpSaved = request.getSession().getAttribute("otp");
        Object emailSaved = request.getSession().getAttribute("otpEmail");

        if (otpSaved == null || emailSaved == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Your OTP session has expired. Please request a new code.");
        }

        if (otpSaved.equals(otpInput)) {
            return ResponseEntity.ok("OTP verified successfully for email: " + emailSaved);
        }

        return ResponseEntity.badRequest().body("Invalid OTP code");
    }

    /**
     * POST /api/auth/reset-password
     * Reset user password after successful OTP verification
     */
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody ResetPasswordRequest resetRequest, HttpServletRequest request) {

        Object emailSaved = request.getSession().getAttribute("otpEmail");
        if (emailSaved == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("OTP verification required before resetting password.");
        }

        boolean updated = userService.updatePasswordByEmail(emailSaved.toString(), resetRequest.newPassword());
        if (!updated) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update password. Please try again later.");
        }

        request.getSession().removeAttribute("otp");
        request.getSession().removeAttribute("otpEmail");

        return ResponseEntity.ok("Password has been successfully reset for " + emailSaved + ".");
    }
}
