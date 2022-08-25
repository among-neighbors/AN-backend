package com.knud4.an.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knud4.an.utils.api.ApiUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        logger.error("접근 권한이 없습니다.", accessDeniedException);
        String body = objectMapper
                .writeValueAsString(ApiUtil.error(HttpServletResponse.SC_UNAUTHORIZED, "접근 권한이 없습니다."));

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(body);
    }
}
