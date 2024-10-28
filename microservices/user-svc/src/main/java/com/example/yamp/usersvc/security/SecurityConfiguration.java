package com.example.yamp.usersvc.security;

import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
  @Bean
  @Profile({"pre-prod", "prod"})
  SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable);
    http.cors(AbstractHttpConfigurer::disable);
    http.oauth2ResourceServer(resource -> resource.jwt(withDefaults()));
    http.authorizeHttpRequests(
        authorize ->
            authorize
                .requestMatchers(HttpMethod.POST, "/register")
                .permitAll()
                .requestMatchers("**/ui-docs/**")
                .permitAll()
                .requestMatchers("**/api-docs/**")
                .permitAll()
                .requestMatchers("/actuator/prometheus")
                .permitAll()
                .requestMatchers("/actuator/health/readiness")
                .permitAll()
                .requestMatchers("/actuator/health/liveness")
                .permitAll()
                .requestMatchers("/actuator/**")
                .hasAnyRole("ACTUATOR", "ADMIN")
                .requestMatchers("**/swagger-ui/**")
                .permitAll()
                    .requestMatchers( HttpMethod.POST, "/api/v1/users")
                    .permitAll()

                .anyRequest()
                .authenticated());
    http.sessionManagement(
        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
  }

  @Bean
  @Profile({"local-dev", "integration-test"})
  // I use this setting for developing only, does not use this in production
  SecurityFilterChain apiFilterChain_dev(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
    http.csrf(AbstractHttpConfigurer::disable);
    http.cors(AbstractHttpConfigurer::disable);
    http.sessionManagement(
        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    http.anonymous(
        anonymous -> {
          anonymous.principal("1a35d863-0cd9-4bc1-8cc4-f4cddca97720");
        });
    return http.build();
  }
}
