package com.knud4.an.utils.cookie;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class CookieUtil {

    private final int COOKIE_VALIDATION_SECOND = 1000 * 60 * 60 * 48;

    public Cookie createCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);

        return cookie;
    }

    public Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(COOKIE_VALIDATION_SECOND);

        return cookie;
    }

    public Cookie getCookie(HttpServletRequest req, String name) {
        Cookie[] findCookies = req.getCookies();
        if (findCookies == null) return null;
        for (Cookie cookie : findCookies) {
            if (cookie.getName().equals(name)) return cookie;
        }
        return null;
    }
}