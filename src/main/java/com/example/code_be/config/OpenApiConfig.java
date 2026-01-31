package com.example.code_be.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("OurLove API")
                                                .description("API cho ứng dụng couple - Lưu giữ kỷ niệm tình yêu 💕")
                                                .version("1.0.0")
                                                .contact(new Contact()
                                                                .name("OurLove Team")
                                                                .email("love@ourlove.app")))
                                .tags(List.of(
                                                new Tag().name("Auth").description("Xác thực người dùng"),
                                                new Tag().name("Home").description("Dashboard và thông tin tổng hợp"),
                                                new Tag().name("Photos").description("Quản lý ảnh và album"),
                                                new Tag().name("Posts").description("Nhật ký / Bài viết"),
                                                new Tag().name("Letters").description("Thư bí mật"),
                                                new Tag().name("Milestones").description("Mốc kỷ niệm"),
                                                new Tag().name("Mood").description("Theo dõi cảm xúc"),
                                                new Tag().name("Quick Messages").description("Tin nhắn nhanh")));
        }
}
