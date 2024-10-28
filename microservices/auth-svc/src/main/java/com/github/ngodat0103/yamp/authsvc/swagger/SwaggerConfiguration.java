package com.github.ngodat0103.yamp.authsvc.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition
@SecurityScheme(
    name = "oauth2",
    type = SecuritySchemeType.OAUTH2,
    flows =
        @OAuthFlows(
            authorizationCode =
                @OAuthFlow(
                    authorizationUrl =
                        "${spring.security.oauth2.client.provider.auth-service-external.authorization-uri}",
                    tokenUrl =
                        "${spring.security.oauth2.client.provider.auth-service-external.token-uri}")),
    bearerFormat = "JWT")
public class SwaggerConfiguration {}
