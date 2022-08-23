package com.football.footballstanding.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiSwaggerConfig {

    private static final String TITLE = "Real Time Football Data Service";
    private static final String DESCRIPTION = "Real Time Football Score Service";
    private static final String VERSION = "1.0.0";
    private static final String CONTACT_NAME = "Hackathon Team9";
    private static final String CONTACT_URL = "mailto:jshubham@live.in";
    private static final String CONTACT_EMAIL = "jshubham@live.in";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(this.getInfo());
    }

    private Info getInfo() {
        return new Info().title(TITLE).description(DESCRIPTION).version(VERSION).contact(this.getContact());
    }

    private Contact getContact() {
        return new Contact().name(CONTACT_NAME).url(CONTACT_URL).email(CONTACT_EMAIL);
    }
}
