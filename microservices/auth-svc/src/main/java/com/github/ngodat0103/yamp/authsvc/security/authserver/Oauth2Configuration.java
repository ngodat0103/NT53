package com.github.ngodat0103.yamp.authsvc.security.authserver;

import com.github.ngodat0103.yamp.authsvc.persistence.repository.UserRepository;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import java.text.ParseException;
import java.util.UUID;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.util.Assert;

@Configuration
@Setter
public class Oauth2Configuration {

  // Create a JWKSource bean that reads the JWK from the k8s secret,
  // If not present, the JwkSource random key will be used base on spring autoconfiguration
  @Bean
  @ConditionalOnProperty(prefix = "k8s.secret", name = "jwk")
  JWKSource<SecurityContext> jwkSourceFromK8sSecret(@Value("${k8s.secret.jwk}") String jwk)
      throws ParseException {
    Assert.notNull(jwk, "jwk must not be null");
    JWKSet jwkSet = new JWKSet(RSAKey.parse(jwk));
    return new ImmutableJWKSet<>(jwkSet);
  }

  @Bean
  public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer(
      UserRepository userRepository) {
    return context -> {
      if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
        UUID uuid;
        // Catch exception if clientCredential is used
        try {
          uuid = UUID.fromString(context.getPrincipal().getName());
        } catch (IllegalArgumentException e) {
          return;
        }
        context
            .getClaims()
            .claims(
                claims -> {
                  userRepository
                      .findById(uuid)
                      .ifPresent(account -> claims.put("role", account.getRole().getRoleName()));
                });
      }
    };
  }
}
