package com.knud4.an.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knud4.an.exception.handler.JwtAccessDeniedHandler;
import com.knud4.an.exception.handler.JwtNotAuthenticatedHandler;
import com.knud4.an.security.filter.JwtAuthenticationFilter;
import com.knud4.an.security.filter.JwtExceptionFilter;
import com.knud4.an.security.filter.JwtProfileAuthenticationFilter;
import com.knud4.an.security.provider.JwtProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 서버 Spring Security FilterChain 설정
 */
@EnableWebSecurity
public class SecurityConfig {

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

    JwtExceptionFilter jwtExceptionFilter(ObjectMapper objectMapper) {
        return new JwtExceptionFilter(objectMapper);
    }

    @Bean
    public SecurityFilterChain accountFilterChain(HttpSecurity http,
                                           JwtProvider jwtProvider, ObjectMapper objectMapper) throws Exception {
        return setJwtHttpSecurity(http, objectMapper)
                .requestMatchers()
                .antMatchers("/api/v1/manager/**")
                .antMatchers("/api/v1/auth/profiles/**")
                .antMatchers("/api/v1/accounts/**")
                .antMatchers("/api/v1/reports/**")
                .antMatchers(HttpMethod.GET,"/api/v1/communities")
                .antMatchers(HttpMethod.GET,"/api/v1/communities/{id}")
                .antMatchers(HttpMethod.GET,"/api/v1/notices/**")
                .antMatchers(HttpMethod.GET,"/api/v1/comments/**")
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/manager/**").hasRole("MANAGER")
                .antMatchers("/api/v1/auth/profiles/**").hasAnyRole("USER", "MANAGER")
                .antMatchers("/api/v1/accounts/**").hasAnyRole("USER", "MANAGER")
                .antMatchers("/api/v1/communities/**").hasAnyRole("USER", "MANAGER")
                .antMatchers("/api/v1/notices/**").hasAnyRole("USER", "MANAGER")
                .antMatchers("/api/v1/comments/**").hasAnyRole("USER", "MANAGER")
                .antMatchers("/api/v1/reports/**").hasAnyRole("USER", "MANAGER")
                .and()
                .addFilterAfter(jwtAuthenticationFilter(jwtProvider),
                        JwtExceptionFilter.class)
                .build();
    }

    @Bean
    public SecurityFilterChain profileFilterChain(HttpSecurity http,
                                           JwtProvider jwtProvider, ObjectMapper objectMapper) throws Exception {
        return setJwtHttpSecurity(http, objectMapper)
                .requestMatchers()
                .antMatchers("/api/v1/profiles/**")
                .antMatchers("/api/v1/communities/**")
                .antMatchers("/api/v1/comments/**/")
                .antMatchers(HttpMethod.POST, "/api/v1/notices")
                .antMatchers("/api/v1/expressions/**")
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/profiles/**").hasAnyRole("USER", "MANAGER")
                .antMatchers( "/api/v1/communities/**").hasAnyRole("USER", "MANAGER")
                .antMatchers( "/api/v1/comments/**").hasAnyRole("USER", "MANAGER")
                .antMatchers( "/api/v1/notices").hasAnyRole("USER", "MANAGER")
                .antMatchers( "/api/v1/expressions/**").hasAnyRole("USER", "MANAGER")
                .and()
                .addFilterAfter(jwtProfileAuthenticationFilter(jwtProvider),
                        JwtExceptionFilter.class)
                .build();
    }

    private HttpSecurity setJwtHttpSecurity(HttpSecurity http, ObjectMapper objectMapper) throws Exception{
        return http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new JwtNotAuthenticatedHandler(new ObjectMapper()))
                .accessDeniedHandler(new JwtAccessDeniedHandler(new ObjectMapper()))
                .and()
                .addFilterBefore(jwtExceptionFilter(objectMapper), UsernamePasswordAuthenticationFilter.class);
    }
}
