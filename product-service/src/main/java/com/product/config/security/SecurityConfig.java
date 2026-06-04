package com.product.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.product.config.jwt.JwtAuthFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtFilter;

    public SecurityConfig(JwtAuthFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfig corsConfig) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth

                // Permitir preflight y endpoints técnicos
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/error", "/swagger-ui/**", "/v3/api-docs/**",
                        "/actuator/info", "/actuator/health").permitAll()

                // CUSTOMER y ADMIN
                .requestMatchers(HttpMethod.GET, "/category/active").hasAnyAuthority("Customer", "Admin")
                .requestMatchers(HttpMethod.GET, "/product/*").hasAnyAuthority("Customer", "Admin")
                .requestMatchers(HttpMethod.GET, "/product/*/image").hasAnyAuthority("Customer", "Admin")
                .requestMatchers(HttpMethod.GET, "/product/*").permitAll()

                // ADMIN
                .requestMatchers("/category/**").hasAuthority("Admin")
                .requestMatchers("/product/**").hasAuthority("Admin")

                // Todo lo demás requiere autenticación
                .anyRequest().authenticated()
            )
            .cors(cors -> cors.configurationSource(corsConfig))
            .formLogin(form -> form.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}