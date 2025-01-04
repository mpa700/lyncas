package com.lyncas.lyncas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication(scanBasePackages = "com.lyncas.lyncas")
@OpenAPIDefinition(info = @Info(title = "API de Contas a Pagar", version = "1.0", description = "Documentação da API"))
@EnableWebMvc
public class LyncasApplication {

	public static void main(String[] args) {
		SpringApplication.run(LyncasApplication.class, args);
	}

}
