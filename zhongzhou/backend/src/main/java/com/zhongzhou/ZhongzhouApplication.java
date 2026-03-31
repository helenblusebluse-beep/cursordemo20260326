package com.zhongzhou;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zhongzhou.modules")
public class ZhongzhouApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZhongzhouApplication.class, args);
    }
}
