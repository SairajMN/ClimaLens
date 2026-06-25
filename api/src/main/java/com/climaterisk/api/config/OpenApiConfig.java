package com.climaterisk.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration.
 * <p>
 * Configures Swagger UI for API documentation.
 * </p>
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hyperlocal Climate Risk Predictor API")
                        .description("REST API for street-level flood and heat risk intelligence")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("ClimaLens Team")
                                .email("support@climalens.example.com")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local development server")));
    }
}