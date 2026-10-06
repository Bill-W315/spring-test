package com.example.mock.order.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ResponseUtil {

    private ResponseUtil() {
    }

    /** Builds a response body with code 0 and {@code data} set to the translated items. */
    public static <E, M, R> ResponseEntity<R> createSuccessResponse(List<E> items, Function<E, M> translator, Class<R> bodyClass) {
        List<M> data = items.stream().map(translator).collect(Collectors.toList());
        try {
            R body = bodyClass.getDeclaredConstructor().newInstance();
            bodyClass.getMethod("setCode", Integer.class).invoke(body, 0);
            bodyClass.getMethod("setData", List.class).invoke(body, data);
            return new ResponseEntity<>(body, HttpStatus.OK);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Cannot build response " + bodyClass.getName(), e);
        }
    }
}
