package com.knud4.an.config;

import com.knud4.an.exception.handler.JwtAccessDeniedHandler;
import com.knud4.an.exception.handler.JwtNotAuthenticatedHandler;
import com.knud4.an.security.filter.JwtAuthenticationFilter;
import com.knud4.an.security.filter.JwtProfileAuthenticationFilter;
import com.knud4.an.utils.cookie.CookieUtil;
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
                                           JwtProvider jwtProvider) throws Exception {
        return setJwtHttpSecurity(http)
                .requestMatchers()
                .antMatchers("/api/v1/auth/profiles/**")
                .antMatchers("/api/v1/accounts/**")
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/auth/profiles/**").hasAnyRole("USER", "MANAGER")
                .antMatchers("/api/v1/accounts/**").hasAnyRole("USER", "MANAGER")
                .and()
                .addFilterBefore(jwtAuthenticationFilter(jwtProvider),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public SecurityFilterChain profileFilterChain(HttpSecurity http,
                                           JwtProvider jwtProvider) throws Exception {
        return setJwtHttpSecurity(http)
                .requestMatchers()
                .antMatchers("/api/v1/profiles/**")
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/profiles/**").hasRole("USER")
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
                .antMatchers("/api/v1/manager/**").hasRole("MANAGER")
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new JwtNotAuthenticatedHandler())
                .accessDeniedHandler(new JwtAccessDeniedHandler())
                .and();
    }
}
