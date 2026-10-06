package com.example.mock.order.entity.enumeration;

import java.util.Arrays;

public enum OrderStatus implements AbstractEnum<Integer> {
    CREATED(1),
    PAID(2),
    SHIPPED(3),
    CANCELLED(4);

    private final Integer code;

    OrderStatus(Integer code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public static OrderStatus fromCode(Integer code) {
        return Arrays.stream(values()).filter(s -> s.code.equals(code)).findFirst().orElse(null);
    }
}
