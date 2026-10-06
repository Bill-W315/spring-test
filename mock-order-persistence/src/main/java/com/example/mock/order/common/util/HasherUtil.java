package com.example.mock.order.common.util;

/**
 * Turns numeric database ids into the opaque ids used by the API and back.
 * Mock implementation: base-36 of (id XOR a fixed mask) with an "o" prefix. Not a security measure.
 */
public final class HasherUtil {

    private static final long MASK = 0x5DEECE66DL;
    private static final String PREFIX = "o";

    private HasherUtil() {
    }

    public static String hash(Long id) {
        return id == null ? null : PREFIX + Long.toString(id ^ MASK, 36);
    }

    public static Long unhash(String hashed) {
        if (hashed == null || !hashed.startsWith(PREFIX)) {
            throw new IllegalArgumentException("Invalid id: " + hashed);
        }
        return Long.parseLong(hashed.substring(PREFIX.length()), 36) ^ MASK;
    }
}
