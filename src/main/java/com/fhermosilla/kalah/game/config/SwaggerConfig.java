package com.fhermosilla.kalah.game.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author felipehermosilla
 * This class configure the swagger configuration
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {
	@Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.fhermosilla.kalah.game.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "Kala Game API",
                "This application provides an API for playing a Kalah game.",
                "1.0.0",
                "Term of service",
                new Contact("Felipe Hermosilla", "https://www.linkedin.com/in/felipe-hermosilla-78918b8b/", "feliphermosilla@gmail.com"),
                "",
                "",
                Collections.emptyList()
        );
    }
}
