package com.aswin.taskboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      // CORS must be evaluated before auth to allow browser preflights
      .cors(cors -> cors.configurationSource(corsConfigurationSource())) // [CORS]
      // For stateless REST/JWT APIs, disable CSRF (enable if using cookies+CSRF tokens)
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth
        // Always allow preflight requests
        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        // Public endpoints (health, root, auth)
        .requestMatchers("/", "/actuator/health", "/auth/register", "/auth/login", "/h2-console/**").permitAll()
        // Example: keep business APIs secured; adjust as needed
        .anyRequest().authenticated()
      )
      // Needed only if H2 console is used
      .headers(headers -> headers.frameOptions(frame -> frame.disable()));
    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration cfg = new CorsConfiguration();

    // Preferred: set FRONTEND_ORIGIN in Render, e.g., https://your-frontend.netlify.app
    String fe = System.getenv("FRONTEND_ORIGIN");
    if (fe != null && !fe.isBlank()) {
      cfg.setAllowedOriginPatterns(List.of(fe));
    } else {
      // Safe defaults for local + typical hosted frontends
      cfg.setAllowedOriginPatterns(List.of(
        "http://localhost:3000",
        "http://localhost:5173",
        "https://*.netlify.app",
        "https://*.onrender.com"
      ));
    }

    cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    cfg.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
    cfg.setExposedHeaders(List.of("Authorization", "Location"));
    cfg.setAllowCredentials(true);
    cfg.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", cfg);
    return source;
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
