package com.knud4.an.config;

import com.knud4.an.exception.CustomAccessDeniedHandler;
import com.knud4.an.security.filter.JwtAuthenticationFilter;
import com.knud4.an.security.filter.JwtProfileAuthenticationFilter;
import com.knud4.an.utils.cookie.CookieUtil;
import com.knud4.an.security.provider.JwtProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableWebSecurity
public class SecurityConfigure {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    JwtAuthenticationFilter jwtAuthenticationFilter(JwtProvider jwtProvider) {
        return new JwtAuthenticationFilter(jwtProvider);
    }

    JwtProfileAuthenticationFilter jwtProfileAuthenticationFilter(JwtProvider jwtProvider) {
        return new JwtProfileAuthenticationFilter(jwtProvider);
    }

    @Bean
    public SecurityFilterChain accountFilterChain(HttpSecurity http,
                                           JwtProvider jwtProvider,
                                           CookieUtil cookieUtil) throws Exception {
        return setJwtHttpSecurity(http)
                .requestMatchers()
                .antMatchers("/api/v1/auth/profile/**")
                .antMatchers("/api/v1/account/**")
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/auth/profile/**").hasRole("USER")
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                        response.sendError(403, "인증 토큰 정보가 잘못되었습니다.");
                    }
                })
                .and()
                .addFilterBefore(jwtAuthenticationFilter(jwtProvider),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public SecurityFilterChain profileFilterChain(HttpSecurity http,
                                           JwtProvider jwtProvider,
                                           CookieUtil cookieUtil) throws Exception {
        return setJwtHttpSecurity(http)
                .requestMatchers()
                .antMatchers("/api/v1/profile/**")
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/profile/**").hasRole("USER")
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                        response.sendError(403, "인증 토큰 정보가 잘못되었습니다.");
                    }
                })
                .and()
                .addFilterBefore(jwtProfileAuthenticationFilter(jwtProvider),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    private HttpSecurity setJwtHttpSecurity(HttpSecurity http) throws Exception{
        return http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .and();
    }
}
