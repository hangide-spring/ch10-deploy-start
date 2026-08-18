package com.metacoding.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        // TODO 7: OpenAPI 문서 정보와 JWT Authorize 버튼을 구성하세요
        //  - new OpenAPI().info(new Info().title("blog API").version("v1").description(...))
        //  - .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"))
        //  - .components(new Components().addSecuritySchemes("bearer-jwt",
        //        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
        //  필요한 import: io.swagger.v3.oas.models.{Components, info.Info, security.SecurityRequirement, security.SecurityScheme}
        //  완성하면 /swagger-ui.html 우측 상단에 Authorize 버튼이 생긴다
        return new OpenAPI(); // 시작 상태 — 문서 제목 없음, Authorize 버튼 없음 (Swagger UI 자체는 뜬다)
    }
}
