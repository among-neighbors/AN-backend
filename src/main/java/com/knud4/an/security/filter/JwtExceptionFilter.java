package com.knud4.an.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.knud4.an.exception.TokenNotFoundException;
import com.knud4.an.utils.api.ApiUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 토큰 관련 예외 핸들링
 */
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        HttpServletResponse res = (HttpServletResponse)response;

        if (request.getMethod().equals(HttpMethod.OPTIONS.toString())) {
            return;
        }

        try {
            filterChain.doFilter(request, response);
        } catch(JWTVerificationException e) {
            String body = objectMapper
                    .writeValueAsString(ApiUtil.error(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰."));

            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
            res.setCharacterEncoding("UTF-8");
            res.getWriter().write(body);
        } catch (TokenNotFoundException e) {
            String body = objectMapper
                    .writeValueAsString(ApiUtil.error(HttpServletResponse.SC_BAD_REQUEST, "토큰이 없습니다."));

            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
            res.setCharacterEncoding("UTF-8");
            res.getWriter().write(body);
        }
    }
}
