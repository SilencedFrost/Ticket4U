package com.ticket4u.mailservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String API_KEY = "X-API-KEY";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mail Service API")
                        .description("API gửi email qua Brevo SMTP")
                        .version("1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList(API_KEY))
                .components(new Components()
                        .addSecuritySchemes(API_KEY, new SecurityScheme()
                                .name(API_KEY)
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .description("API Key để xác thực. VD: sk_user_dev_...")));
    }
}
