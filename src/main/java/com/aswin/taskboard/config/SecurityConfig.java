package com.aswin.taskboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/auth/register",
                    "/auth/login",
                    "/h2-console/**" // remove in production if not needed
                ).permitAll()
                .requestMatchers("/tasks/**").authenticated()
                .anyRequest().authenticated()
            )
            // allow H2 console frames only if you still use it locally
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Allow your deployed frontend and optionally local dev
        config.setAllowedOrigins(List.of(
            "https://precious-florentine-247c8c.netlify.app",
            "http://localhost:5173"
        ));

        // Methods your API supports
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Allow any headers (or specify explicitly if you prefer)
        config.setAllowedHeaders(List.of("*"));

        // If you use cookies/session-based auth, keep true. If purely JWT in headers, can be false.
        config.setAllowCredentials(true);

        // Expose headers if your frontend needs to read them
        config.setExposedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
