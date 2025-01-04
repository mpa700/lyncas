package com.lyncas.lyncas.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "API Contas a Pagar",
        version = "v1",
        description = "API paroa gerenciamento de contas a pagar"
    )
)
public class OpenApiConfig {
	
	@Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
            .group("public")
            .packagesToScan("com.lyncas.lyncas.controller")
            .build();
    }
	
}