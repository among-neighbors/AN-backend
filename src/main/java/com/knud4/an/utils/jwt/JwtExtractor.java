package com.knud4.an.utils.jwt;

import org.apache.logging.log4j.util.Strings;

import javax.servlet.http.HttpServletRequest;

public class JwtExtractor {

    public static final String JWT_HEADER_NAME = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";

    public static String extractJwt(HttpServletRequest req) {
        String authHeader = req.getHeader(JWT_HEADER_NAME);
        if (authHeader != null && authHeader.startsWith(JWT_PREFIX)) {
            return authHeader.replace(JWT_PREFIX, "");
        }

        return Strings.EMPTY;
    }
}
