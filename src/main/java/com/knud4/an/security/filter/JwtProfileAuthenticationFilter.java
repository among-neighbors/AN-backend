package com.knud4.an.security.filter;

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

@RequiredArgsConstructor
public class JwtProfileAuthenticationFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String token;
        Authentication authenticate;

        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;

        token = JwtExtractor.extractJwt(req);

        if(token != null && !jwtProvider.isProfileTokenExpired(token)) {
            try {
                String emailFromToken = jwtProvider.getEmailFromToken(token);
                authenticate = jwtProvider
                        .authenticate(new UsernamePasswordAuthenticationToken(emailFromToken, ""));
                SecurityContextHolder.getContext().setAuthentication(authenticate);
            } catch(Exception e) {
            }
        }

        chain.doFilter(request, response);
    }
}
