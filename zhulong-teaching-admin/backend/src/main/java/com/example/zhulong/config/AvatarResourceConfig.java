package com.example.zhulong.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 本机头像目录映射到 {@code /uploads/avatars/**}，与 {@link AvatarProperties#getLocalUrlPrefix()} 一致。
 */
@Configuration
public class AvatarResourceConfig implements WebMvcConfigurer {

    private final AvatarProperties avatarProperties;

    public AvatarResourceConfig(AvatarProperties avatarProperties) {
        this.avatarProperties = avatarProperties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (avatarProperties.isSftpEnabled()) {
            return;
        }
        Path dir = Paths.get(avatarProperties.getLocalDirectory()).toAbsolutePath().normalize();
        String location = dir.toUri().toString();
        if (!location.endsWith("/")) {
            location = location + "/";
        }
        registry.addResourceHandler("/uploads/avatars/**").addResourceLocations(location);
    }
}
