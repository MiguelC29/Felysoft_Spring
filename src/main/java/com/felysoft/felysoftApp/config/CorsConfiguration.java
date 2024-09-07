package com.felysoft.felysoftApp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://felysoft-react.vercel.app")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT")
                .allowedHeaders("Authorization", "Content-Type") // Permitir el envío del JWT y otros headers
                .exposedHeaders("Authorization") // Exponer el header si necesitas que el frontend lo lea en la respuesta
                .maxAge(3600); // Tiempo de caché de las políticas de CORS
    }
}
