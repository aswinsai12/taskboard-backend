package com.aswin.taskboard.config;

import jakarta.servlet.DispatcherType;
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
      .cors(c -> c.configurationSource(corsConfigurationSource()))
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth
        // allow error dispatches and preflights
        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        // public endpoints
        .requestMatchers("/", "/error", "/actuator/health", "/auth/register", "/auth/login", "/h2-console/**").permitAll()
        // everything else requires auth
        .anyRequest().authenticated()
      )
      .headers(h -> h.frameOptions(f -> f.disable())); // only for H2 console
    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration cfg = new CorsConfiguration();

    // Exact frontend origin from env (preferred)
    String fe = System.getenv("FRONTEND_ORIGIN");
    if (fe != null && !fe.isBlank()) {
      cfg.setAllowedOriginPatterns(List.of(fe));
    } else {
      // Fallbacks for local/dev hosts
      cfg.setAllowedOriginPatterns(List.of(
        "http://localhost:3000",
        "http://localhost:5173",
        "https://*.netlify.app",
        "https://*.onrender.com"
      ));
    }

    cfg.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
    cfg.setAllowedHeaders(List.of("Authorization","Content-Type","X-Requested-With"));
    cfg.setExposedHeaders(List.of("Authorization","Location"));
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
