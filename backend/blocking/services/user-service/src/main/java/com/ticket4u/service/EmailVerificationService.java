package com.ticket4u.service;

import com.ticket4u.entity.User;

public interface EmailVerificationService {
    void sendVerificationEmail(User user);

    void verifyToken(String plainToken);

    void resendVerification(String email);
}
