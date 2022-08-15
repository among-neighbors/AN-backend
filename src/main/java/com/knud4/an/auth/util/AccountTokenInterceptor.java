package com.knud4.an.auth.util;

import com.knud4.an.exception.NotFoundException;
import com.knud4.an.security.provider.JwtProvider;
import com.knud4.an.utils.jwt.JwtExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class AccountTokenInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String accountToken = JwtExtractor.extractJwt(request);

        String email = jwtProvider.getEmailFromToken(accountToken);
        Long accountId = jwtProvider.getAccountIdFromToken(accountToken);

        request.setAttribute("accountId", accountId);
        request.setAttribute("email", email);
        return true;
    }
}
