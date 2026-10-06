package com.example.mock.order.common.auth;

/** Data domains with their own data-rights level. */
public enum DataType {
    ORDER(1);

    private final int value;

    DataType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
