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
                "/api/v1/accounts/**", "/api/v1/auth/profiles/**", "/api/v1/reports/**"
        );

        registry.addInterceptor(profileTokenInterceptor).addPathPatterns(
                "/api/v1/profiles/**"
        );
    }
}
