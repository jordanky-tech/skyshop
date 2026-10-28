package com.jordan.skyshoplink.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Autoriser l'origine du frontend Angular
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));

        // Autoriser les méthodes HTTP nécessaires
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Autoriser les en-têtes (notamment Authorization pour le token JWT et Content-Type)
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));

        // Autoriser l'envoi de cookies ou d'en-têtes d'authentification si besoin
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Appliquer cette configuration à toutes les routes de l'API
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
