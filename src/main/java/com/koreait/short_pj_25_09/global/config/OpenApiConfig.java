package com.koreait.short_pj_25_09.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Short_pj_25_09 API",
                version = "v1",
                description = "Short_pj_25_09 서비스 API 문서",
                contact = @Contact(name = "KoreaIT", email = "admin@example.com")
        ),
        servers = {
                @Server(url = "http://localhost:8070", description = "Local")
        }
)
public class OpenApiConfig {

    // 필요 시 그룹 분리 가능 (패키지 기준 스캔)
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .packagesToScan("com.koreait.short_pj_25_09")
                .build();
    }
}
