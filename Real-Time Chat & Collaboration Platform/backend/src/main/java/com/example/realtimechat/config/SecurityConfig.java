package com.example.realtimechat.config;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  private final String frontendBaseUrl;
  private final String corsAllowedOrigins;

  public SecurityConfig(
      @Value("${app.frontend-base-url}") String frontendBaseUrl,
      @Value("${app.cors-allowed-origins}") String corsAllowedOrigins
  ) {
    this.frontendBaseUrl = frontendBaseUrl;
    this.corsAllowedOrigins = corsAllowedOrigins;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/actuator/**", "/oauth2/**", "/login/**", "/error", "/ws/**").permitAll()
            .anyRequest().authenticated()
        )
        .oauth2Login(oauth -> oauth.defaultSuccessUrl(frontendBaseUrl, true))
        .logout(logout -> logout.logoutSuccessUrl(frontendBaseUrl))
        .csrf(csrf -> csrf.disable())
        .cors(cors -> {});

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(splitOrigins());
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  private List<String> splitOrigins() {
    return Arrays.stream(corsAllowedOrigins.split(","))
        .map(String::trim)
        .filter(origin -> !origin.isEmpty())
        .toList();
  }
}
