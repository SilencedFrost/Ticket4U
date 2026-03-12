package com.ticket4u.service;

public interface PasswordResetService {
    void sendPasswordResetEmail(String email);

    void resetPassword(String plainToken, String newPassword);
}
