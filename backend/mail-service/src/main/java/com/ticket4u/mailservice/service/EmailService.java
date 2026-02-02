package com.ticket4u.mailservice.service;

import com.ticket4u.mailservice.dto.request.EmailRequest;
import com.ticket4u.mailservice.dto.response.EmailResponse;

public interface EmailService {

    EmailResponse send(EmailRequest request);

    EmailResponse sendSync(EmailRequest request);

    void sendAsync(EmailRequest request, String messageId);
}
