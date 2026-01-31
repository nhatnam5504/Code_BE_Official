package com.example.code_be.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // Allow ALL origins
                .allowedMethods("*") // Allow ALL methods
                .allowedHeaders("*") // Allow ALL headers
                .allowCredentials(true)
                .maxAge(3600);
    }
}
