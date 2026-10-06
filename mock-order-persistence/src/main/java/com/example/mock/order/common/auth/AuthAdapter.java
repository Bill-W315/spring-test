package com.example.mock.order.common.auth;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Caller identity for the current request. The mock reads it from headers set by the gateway:
 * {@code X-Company-Id} and {@code X-Employee-Id} (hashed ids) and {@code X-Data-Rights} (data-rights level).
 */
@Component
public class AuthAdapter {

    public Object getAttribute(AttributeType type) {
        return header(type == AttributeType.COMPANY_ID ? "X-Company-Id" : "X-Employee-Id");
    }

    /** Data-rights level of the caller for a data domain (0 = none). */
    public Integer getPermission(int dataType) {
        String rights = header("X-Data-Rights");
        return rights == null ? 0 : Integer.valueOf(rights);
    }

    private static String header(String name) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        HttpServletRequest request = attributes.getRequest();
        return request.getHeader(name);
    }
}
