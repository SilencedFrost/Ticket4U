package com.ticket4u.service;

import com.ticket4u.dto.user.ChangeEmailRequest;

import java.util.UUID;

public interface EmailChangeService {
    void initiateEmailChange(UUID userId, ChangeEmailRequest request);
    void confirmEmailChange(String token);
}
