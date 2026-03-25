package com.ticket4u.mapper;

import com.ticket4u.util.EmailUtil;
import com.ticket4u.util.PhoneNumberUtil;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@Named("UserHelperMapper")
public class UserHelperMapper {

    @Named("extractUsername")
    public String extractUsername(String email) {
        return email != null ? EmailUtil.getLocalPart(email, true) : null;
    }

    @Named("normalizePhone")
    public String normalizePhone(String phone) {
        return phone != null && !phone.isBlank() ? PhoneNumberUtil.normalize(phone) : null;
    }

    @Named("extractFirstName")
    public String extractFirstName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            return null;
        }
        String cleaned = stripParenthetical(fullName);
        if (cleaned.isBlank()) {
            return null;
        }
        String[] names = cleaned.split("\\s+", 2);
        return names[0];
    }

    @Named("extractLastName")
    public String extractLastName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            return null;
        }
        String cleaned = stripParenthetical(fullName);
        if (cleaned.isBlank()) {
            return null;
        }
        String[] names = cleaned.split("\\s+", 2);
        return names.length > 1 ? names[1] : null;
    }

    private String stripParenthetical(String name) {
        return name.replaceAll("\\s*\\(.*?\\)", "").trim();
    }
}
