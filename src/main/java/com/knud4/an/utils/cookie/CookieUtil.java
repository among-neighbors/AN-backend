package com.knud4.an.utils.cookie;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class CookieUtil {

    private final int COOKIE_VALIDATION_SECOND = 1000 * 60 * 60 * 48;

    public ResponseCookie createCookie(String name, String value, int maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .path("/")
                .secure(true)
                .sameSite("None")
                .maxAge(maxAge)
                .build();
    }

    public ResponseCookie createCookie(String name, String value) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .path("/")
                .secure(true)
                .sameSite("None")
                .maxAge(COOKIE_VALIDATION_SECOND)
                .build();
    }

    public ResponseCookie getCookie(HttpServletRequest req, String name) {
        Cookie[] findCookies = req.getCookies();
        if (findCookies == null) return null;
        for (Cookie cookie : findCookies) {
            if (cookie.getName().equals(name)) {
                return ResponseCookie.from(name, cookie.getValue()).build();
            }
        }
        return null;
    }
}