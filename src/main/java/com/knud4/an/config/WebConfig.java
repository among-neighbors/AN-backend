package com.knud4.an.config;

import com.knud4.an.interceptor.AccountTokenInterceptor;
import com.knud4.an.interceptor.ProfileTokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AccountTokenInterceptor accountTokenInterceptor;
    private final ProfileTokenInterceptor profileTokenInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://localhost:5173")
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .allowedHeaders("*")
                .exposedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accountTokenInterceptor).addPathPatterns(
                "/api/v1/accounts/**", "/api/v1/auth/profiles/**", "/api/v1/reports/**",
                "/api/v1/communities/**", "/api/v1/notices/**", "/api/v1/comments/**",
                "/api/v1/manager/notices/new"
        ).excludePathPatterns("/api/v1/communities/new", "/api/v1/notices/new", "/api/v1/comments/**/new",
                "/api/v1/communities/me", "/api/v1/communities/{id}/update", "/api/v1/communities/{id}/delete");

        registry.addInterceptor(profileTokenInterceptor).addPathPatterns(
                "/api/v1/profiles/**", "/api/v1/communities/new", "/api/v1/notices/new", "/api/v1/comments/**/new",
                "/api/v1/communities/me", "/api/v1/communities/{id}/update", "/api/v1/communities/{id}/delete"
        );
    }
}
