package com.ticket4u.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Named("HashUtil")
public class HashUtil {

    public static final int LENGTH = 7;

    public static String toHashId(UUID id, int length) {
        if (id == null) {
            return null;
        }
        return DigestUtils.sha256Hex(id.toString()).substring(0, length);
    }

    @Named("toHashId")
    public static String toHashId(UUID id) {
        return toHashId(id, LENGTH);
    }
}