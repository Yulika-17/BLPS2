package com.nullnumber1.lab1.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "BLPS Api",
                description = "lab2 BLPS", version = "1.0.0",
                contact = @Contact(
                        name = "Yulia Maltseva",
                        url = "https://vk.com/ulika17"
                )
        )
)
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "basicAuth",
        scheme = "basic")
public class SwaggerConfig {

}
