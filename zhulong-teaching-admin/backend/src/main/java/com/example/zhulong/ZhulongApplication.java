package com.example.zhulong;

import com.example.zhulong.config.AvatarProperties;
import com.example.zhulong.config.ZhulongProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@MapperScan("com.example.zhulong.**.mapper")
@EnableConfigurationProperties({ AvatarProperties.class, ZhulongProperties.class })
public class ZhulongApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZhulongApplication.class, args);
    }
}

