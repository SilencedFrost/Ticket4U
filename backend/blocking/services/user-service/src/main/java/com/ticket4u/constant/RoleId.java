package com.ticket4u.constant;

public final class RoleId {
    // Basic roles (hierarchy)
    public static final int CUSTOMER = 0;
    public static final int EVENT_MANAGER = 1;
    public static final int ORGANIZER_ADMIN = 2;
    public static final int ADMIN = 3;
    public static final int SYSTEM_ADMIN = 4;

    // Special roles (no hierarchy)
    public static final int GATEKEEPER = 10;
    public static final int SUPPORT_AGENT = 11;
    public static final int FINANCE_MANAGER = 12;

    private RoleId() {}
}
