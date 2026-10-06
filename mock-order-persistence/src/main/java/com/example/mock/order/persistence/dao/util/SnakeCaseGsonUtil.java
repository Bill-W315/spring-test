package com.example.mock.order.persistence.dao.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class SnakeCaseGsonUtil {

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    private SnakeCaseGsonUtil() {
    }

    public static String toJson(Object value) {
        return GSON.toJson(value);
    }
}
