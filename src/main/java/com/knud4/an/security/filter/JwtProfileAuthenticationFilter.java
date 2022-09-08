package com.knud4.an.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.knud4.an.exception.TokenNotFoundException;
import com.knud4.an.security.provider.JwtProvider;
import com.knud4.an.utils.jwt.JwtExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * spring security 프로필 토큰 검증 필터
 */
@RequiredArgsConstructor
public class JwtProfileAuthenticationFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;


    /**
     * @see JwtExtractor
     * @see org.springframework.security.core.userdetails.UserDetailsService
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException, TokenNotFoundException, JWTVerificationException {

        HttpServletRequest req = (HttpServletRequest)request;

        String token = JwtExtractor.extractJwt(req);

        String email = jwtProvider.getEmailFromToken(token);
        Long profileId = jwtProvider.getProfileIdFromToken(token);
        Long accountId = jwtProvider.getAccountIdFromToken(token);

        request.setAttribute("email", email);
        request.setAttribute("accountId", accountId);
        request.setAttribute("profileId", profileId);

        Authentication authenticate = jwtProvider.authenticate(new UsernamePasswordAuthenticationToken(email, profileId));
        SecurityContextHolder.getContext().setAuthentication(authenticate);


        chain.doFilter(request, response);
    }
}
