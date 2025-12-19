package com.ticket4u.service;

import com.ticket4u.dto.auth.GoogleUserInfo;
import com.ticket4u.dto.auth.LoginRequest;
import com.ticket4u.dto.auth.RegisterRequest;
import com.ticket4u.dto.auth.RegisterResponse;
import com.ticket4u.dto.auth.internal.LoginResult;
import com.ticket4u.dto.auth.internal.RefreshResult;
import jakarta.validation.Valid;

public interface AuthService {
    /**
     * Login with email/password
     */
    LoginResult login(@Valid LoginRequest loginRequest, String oldRefreshToken, String userAgent);

    /**
     * Refresh access token using refresh token
     */
    RefreshResult refresh(String refreshToken);

    /**
     * Register new user with email/password
     * Account will be INACTIVE until email verification
     */
    RegisterResponse registerWithEmail(@Valid RegisterRequest request);

    /**
     * Register new user with Google OAuth2
     * Email is auto-verified, account is ACTIVE immediately
     *
     * @param userInfo verified Google user information
     * @return RegisterResponse with user details
     */
    RegisterResponse registerWithGoogle(GoogleUserInfo userInfo);
}
