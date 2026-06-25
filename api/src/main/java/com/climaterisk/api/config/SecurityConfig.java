package com.climaterisk.api.config;

import com.climaterisk.api.filter.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security configuration with JWT authentication.
 * <p>
 * Defines role-based access:
 * - PUBLIC: read-only access to zones and scores
 * - OFFICIAL: can trigger alerts and view explanations
 * - ADMIN: full access including user management
 * </p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/", "/index", "/favicon.ico").permitAll()
                        .requestMatchers("/api/v1/zones/**").permitAll()
                        .requestMatchers("/api/v1/scores/current/**").permitAll()
                        .requestMatchers("/api/v1/scores/hotspots/**").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()

                        // Protected endpoints
                        .requestMatchers("/api/v1/alerts/**").hasAnyRole("OFFICIAL", "ADMIN")
                        .requestMatchers("/api/v1/explanations/**").hasAnyRole("OFFICIAL", "ADMIN")
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")

                        // Default: require authentication
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}