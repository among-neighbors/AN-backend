package com.knud4.an.utils.jwt;

import com.knud4.an.exception.TokenNotFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * 인증 토큰 추출 보조 클래스
 */
public class JwtExtractor {

    public static final String JWT_HEADER_NAME = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";

    /**
     * {@link HttpServletRequest http request} 인증 header 로 부터 토큰 추출 메소드
     * <p>인증 헤더 형식 {@value JWT_HEADER_NAME}, {@value JWT_PREFIX} 참조</p>
     * @exception TokenNotFoundException
     *              헤더에 토큰이 존재하지 않을 때
     */
    public static String extractJwt(HttpServletRequest req) {
        String authHeader = req.getHeader(JWT_HEADER_NAME);
        if (authHeader != null && authHeader.startsWith(JWT_PREFIX)) {
            return authHeader.replace(JWT_PREFIX, "");
        }
        throw new TokenNotFoundException("헤더에 토큰이 없습니다.");
    }
}
