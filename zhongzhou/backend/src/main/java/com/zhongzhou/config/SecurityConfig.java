package com.zhongzhou.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeRequests(auth -> auth
                        .antMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/api/home/**",
                                "/api/appointments/**",
                                "/api/visits/**",
                                "/api/health-assessments/**",
                                "/api/checkins/**",
                                "/api/checkouts/**",
                                "/api/contracts/**",
                                "/api/customers/**",
                                "/api/nursing-items/**",
                                "/api/nursing-plans/**",
                                "/api/nursing-levels/**",
                                "/api/nursing-care-levels/**",
                                "/api/responsible-elders/**",
                                "/api/task-schedules/**",
                                "/api/orders/**",
                                "/api/refunds/**",
                                "/api/bills/**",
                                "/api/prepay-recharges/**",
                                "/api/balance-queries/**",
                                "/api/arrears-elders/**",
                                "/api/iot/**",
                                "/api/profile/**",
                                "/api/files/**",
                                "/uploads/**",
                                "/api/room-types",
                                "/api/room-types/**",
                                "/api/bed-preview/**",
                                "/api/smart-beds/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated());
        return http.build();
    }
}
