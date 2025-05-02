package com.medicalregister.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/index.xhtml",
                                "/patients.xhtml",
                                "/favicon.ico",
                                "/css/**",
                                "/js/**",
                                "/u/**",            // Auth0 widget & callback
                                "/h2-console/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                )
                .headers(headers -> headers.disable())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt());

        return http.build();
    }




}
