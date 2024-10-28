package com.github.ngodat0103.yamp.authsvc.security;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.resource.authentication.DelegatingJwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

  @Bean
  SecurityFilterChain oauth2FilterChain(HttpSecurity http) throws Exception {
    OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
    http.securityMatcher("/oauth2/**");
    http.getConfigurer(OAuth2AuthorizationServerConfigurer.class).oidc(Customizer.withDefaults());
    http.sessionManagement(
        session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
    http.exceptionHandling(
        e -> e.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")));
    return http.build();
  }

  @Bean
  public SecurityFilterChain loginFilterChain(HttpSecurity http) throws Exception {
    http.formLogin(Customizer.withDefaults());
    http.securityMatcher("/login");
    http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/login").permitAll());
    http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER));
    return http.build();
  }

  @Bean
  @Profile({"pre-prod", "prod"})
  SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable);
    http.cors(AbstractHttpConfigurer::disable);
    http.securityMatcher(
        "/accounts/**", "/roles/**", "/ui-docs/**", "/api-docs/**", "/actuator/**");
    http.oauth2ResourceServer(
        resource ->
            resource.jwt(jwt -> jwt.jwtAuthenticationConverter(getJwtAuthenticationConverter())));
    http.authorizeHttpRequests(
        authorize ->
            authorize
                .requestMatchers("/ui-docs/**")
                .permitAll()
                .requestMatchers("/api-docs/**")
                .permitAll()
                .requestMatchers("/actuator/prometheus")
                .permitAll()
                .requestMatchers("/actuator/health/readiness")
                .permitAll()
                .requestMatchers("/actuator/health/liveness")
                .permitAll()
                .requestMatchers("/actuator/**")
                .hasAnyRole("ACTUATOR", "ADMIN")
                .anyRequest()
                .authenticated());
    http.sessionManagement(
        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
  }

  @Bean
  @Profile({
    "local-dev",
    "integration-test"
  }) // I use this setting for testing and developing only, does not use this in production
  SecurityFilterChain apiFilterChain_dev(HttpSecurity http) throws Exception {
    http.securityMatcher(
        "/accounts/**", "/roles/**", "/ui-docs/**", "/api-docs/**", "/actuator/**");
    http.csrf(AbstractHttpConfigurer::disable);
    http.cors(AbstractHttpConfigurer::disable);
    http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
    http.anonymous(
        anonymous -> {
          String principal = "1a35d863-0cd9-4bc1-8cc4-f4cddca97720";
          anonymous.principal(principal);
          anonymous.authorities(
              "ROLE_ADMIN", "SCOPE_auth-service.read", "SCOPE_auth-service.write");
        });
    http.sessionManagement(
        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
  }

  @Bean
  AuthorizationServerSettings authorizationServerSettings() {
    return AuthorizationServerSettings.builder().build();
  }

  @Bean
  @Profile({"pre-prod", "prod"})
  PasswordEncoder passwordEncoder() {
    return new DelegatingPasswordEncoder("bcrypt", Map.of("bcrypt", new BCryptPasswordEncoder()));
  }

  // I use this for easy development, do not use this in production
  @Bean
  @Profile({"local-dev", "integration-test"})
  PasswordEncoder passwordEncoder_dev() {
    Map<String, PasswordEncoder> passwordEncoderMap = new HashMap<>();
    passwordEncoderMap.put("bcrypt", new BCryptPasswordEncoder());
    passwordEncoderMap.put("noop", NoOpPasswordEncoder.getInstance());
    return new DelegatingPasswordEncoder("bcrypt", passwordEncoderMap);
  }

  public JwtAuthenticationConverter getJwtAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter converterForRoles = new JwtGrantedAuthoritiesConverter();
    converterForRoles.setAuthorityPrefix("ROLE_");
    converterForRoles.setAuthoritiesClaimName("role");
    JwtGrantedAuthoritiesConverter converterForScope = new JwtGrantedAuthoritiesConverter();
    DelegatingJwtGrantedAuthoritiesConverter delegatingJwtGrantedAuthoritiesConverter =
        new DelegatingJwtGrantedAuthoritiesConverter(converterForRoles, converterForScope);
    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
        delegatingJwtGrantedAuthoritiesConverter);
    return jwtAuthenticationConverter;
  }
}
