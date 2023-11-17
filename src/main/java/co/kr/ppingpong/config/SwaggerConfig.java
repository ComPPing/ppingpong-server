//package co.kr.ppingpong.config;
//
//import io.swagger.v3.oas.models.Components;
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.security.SecurityRequirement;
//import io.swagger.v3.oas.models.security.SecurityScheme;
//import io.swagger.v3.oas.models.servers.Server;
//import org.springdoc.core.GroupedOpenApi;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class SwaggerConfig {
//
//    @Bean
//    public OpenAPI openAPI() {
//
//        // SecurityScheme명
//        String jwtSchemeName = "ACCESS_TOKEN";
//
//        // API 요청 헤더에 인증정보 포함
//        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
//
//        //SecuritySchemes 등록
//        Components components = new Components()
//                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
//                        .name(jwtSchemeName)
//                        .type(SecurityScheme.Type.HTTP)
//                        .scheme("bearer") // bearer를 앞에 붙여주는거구나
//                        .bearerFormat("JWT")
//                );
//
//        return new OpenAPI()
//                .addServersItem(new Server().url("/"))
//                .addSecurityItem(securityRequirement)
//                .components(components)
//                .info(apiInfo());
//    }
//
//
//    private Info apiInfo() {
//        return new Info()
//                .title("Ppingpong")
//                .description("api test")
//                .version("1.0.0");
//    }
//}