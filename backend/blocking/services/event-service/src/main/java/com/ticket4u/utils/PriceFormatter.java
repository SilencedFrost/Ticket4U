package com.ticket4u.utils;

import java.text.DecimalFormat;

public class PriceFormatter {
    private static final DecimalFormat formatter = new DecimalFormat("#,###");

    public static String format(Object price) {
        if (price == null) return "0đ";
        return formatter.format(price) + "đ";
    }
}
