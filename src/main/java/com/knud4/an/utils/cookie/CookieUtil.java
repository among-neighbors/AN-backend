package com.knud4.an.utils.cookie;

import com.knud4.an.exception.CookieNotFoundException;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 서버 스펙의 브라우저 쿠키 생성/조회 보조 클래스
 *
 * @see ResponseCookie
 */
@Component
public class CookieUtil {

    /**
     * 쿠키 유효 기간(default)
     */
    private final int COOKIE_VALIDATION_SECOND = 1000 * 60 * 60 * 48;

    /**
     * 유효기간을 설정할 수 있는 브라우저 쿠키 생성
     * @param name 쿠키 이름
     * @param value 쿠키 값
     * @param maxAge 쿠키 유효 기간
     */
    public ResponseCookie createCookie(String name, String value, int maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .path("/")
                .secure(true)
                .sameSite("None")
                .maxAge(maxAge)
                .build();
    }

    /**
     * 서버 스펙의 브라우저 쿠키 생성
     * @param name 쿠키 이름
     * @param value 쿠키 값
     */
    public ResponseCookie createCookie(String name, String value) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .path("/")
                .secure(true)
                .sameSite("None")
                .maxAge(COOKIE_VALIDATION_SECOND)
                .build();
    }

    /**
     * {@link HttpServletRequest http request} 에 포함된 브라우저 쿠키 조회
     * @param req http request
     * @param name 쿠키 이름
     * @exception  CookieNotFoundException
     *              요청한 쿠키가 없을 때
     */
    public ResponseCookie getCookie(HttpServletRequest req, String name) {
        Cookie[] findCookies = req.getCookies();
        if (findCookies == null) {
         throw new CookieNotFoundException("전달된 쿠키가 없습니다.");
        }
        for (Cookie cookie : findCookies) {
            if (cookie.getName().equals(name)) {
                return ResponseCookie.from(name, cookie.getValue()).build();
            }
        }

        throw new CookieNotFoundException("쿠키를 찾을 수 없습니다.");
    }
}