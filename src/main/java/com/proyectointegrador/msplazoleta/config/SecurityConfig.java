package com.proyectointegrador.msplazoleta.config;

import com.proyectointegrador.msplazoleta.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/error").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/restaurantes/**", "/api/platos/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/restaurantes").hasRole("ADMINISTRADOR")
                    .requestMatchers(HttpMethod.POST, "/api/empleados").hasRole("PROPIETARIO")
                    .requestMatchers(HttpMethod.POST, "/api/platos").hasRole("PROPIETARIO")
                    .requestMatchers(HttpMethod.PATCH, "/api/platos/**").hasRole("PROPIETARIO")
                    .requestMatchers(HttpMethod.POST, "/api/pedidos").hasRole("CLIENTE")
                    .anyRequest().authenticated()
            )
            .addFilterBefore(
                    jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class
            );
        return http.build();
    }
}
