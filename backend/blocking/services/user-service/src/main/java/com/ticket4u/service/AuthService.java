package com.ticket4u.service;

import com.ticket4u.dto.auth.LoginRequest;
import com.ticket4u.dto.auth.RegisterRequest;
import com.ticket4u.dto.auth.RegisterResponse;
import com.ticket4u.dto.auth.internal.LoginResult;
import com.ticket4u.dto.auth.internal.LogoutResult;
import com.ticket4u.dto.auth.internal.RefreshResult;
import jakarta.validation.Valid;

public interface AuthService {
    LoginResult login(@Valid LoginRequest loginRequest, String oldRefreshToken, String userAgent);

    LogoutResult logout(String refreshToken);

    RefreshResult refresh(String refreshToken);

    RegisterResponse registerWithEmail(@Valid RegisterRequest request);

    RegisterResponse registerWithGoogle(String idToken);
}
