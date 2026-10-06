package com.example.mock.order.common.dao;

import org.jooq.types.UInteger;

/** Conversions for unsigned stored-procedure parameters. */
public final class StoredProcedureUtil {

    private StoredProcedureUtil() {
    }

    public static UInteger longToUInt(Long value) {
        return value == null ? null : UInteger.valueOf(value);
    }

    public static UInteger integerToUInteger(Integer value) {
        return value == null ? null : UInteger.valueOf(value);
    }
}
