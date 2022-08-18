package com.knud4.an.interceptor;

import com.auth0.jwt.exceptions.JWTVerificationException;
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
public class ProfileTokenInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String profileToken = JwtExtractor.extractJwt(request);
        try {
            String email = jwtProvider.getEmailFromToken(profileToken);
            Long profileId = jwtProvider.getProfileIdFromToken(profileToken);
            Long accountId = jwtProvider.getAccountIdFromToken(profileToken);

            request.setAttribute("email", email);
            request.setAttribute("accountId", accountId);
            request.setAttribute("profileId", profileId);
        } catch (JWTVerificationException e) {
        }
        return true;
    }
}
