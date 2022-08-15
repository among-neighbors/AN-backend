package com.knud4.an.config;

import com.knud4.an.auth.util.AccountTokenInterceptor;
import com.knud4.an.auth.util.ProfileTokenInterceptor;
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
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .allowedHeaders("*")
                .exposedHeaders("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accountTokenInterceptor).addPathPatterns(
                "/api/v1/account/**", "/api/v1/auth/profile/**"
        );

        registry.addInterceptor(profileTokenInterceptor).addPathPatterns(
                "/api/v1/profile/**"
        );
    }
}
