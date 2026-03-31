package com.zhongzhou.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI zhongzhouOpenApi() {
        return new OpenAPI().info(new Info()
                .title("中州养老管理平台 API")
                .description("首页数据看板及基础业务接口")
                .version("v1.0.0"));
    }
}
