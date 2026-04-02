package com.ticket4u.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Tính displayId từ sessionId.
 * Là Spring Component để MapStruct có thể dùng trực tiếp qua `uses`.
 * displayId = SHA256(sessionId.toString()).substring(0, LENGTH), case-sensitive.
 */
@Component
@Named("SessionIdHashUtil")
public class SessionIdHashUtil {

    public static final int LENGTH = 6;

    @Named("toDisplayId")
    public static String toDisplayId(UUID sessionId) {
        if (sessionId == null) {
            return null;
        }
        return DigestUtils.sha256Hex(sessionId.toString()).substring(0, LENGTH);
    }
}