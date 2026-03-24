package com.example.zhulong.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "zhulong")
public class ZhulongProperties {

    private final Jwt jwt = new Jwt();
    private final Auth auth = new Auth();

    public Jwt getJwt() {
        return jwt;
    }

    public Auth getAuth() {
        return auth;
    }

    public static class Jwt {
        /** HS256 密钥，建议 ≥32 字节，可用环境变量覆盖 */
        private String secret = "zhulong-teaching-jwt-secret-min-32-chars-long!!";
        private long expireHours = 24;

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public long getExpireHours() {
            return expireHours;
        }

        public void setExpireHours(long expireHours) {
            this.expireHours = expireHours;
        }
    }

    public static class Auth {
        private String adminUsername = "root";
        private String adminPassword = "root";
        private String adminName = "超级管理员";
        private long adminId = 17;

        public String getAdminUsername() {
            return adminUsername;
        }

        public void setAdminUsername(String adminUsername) {
            this.adminUsername = adminUsername;
        }

        public String getAdminPassword() {
            return adminPassword;
        }

        public void setAdminPassword(String adminPassword) {
            this.adminPassword = adminPassword;
        }

        public String getAdminName() {
            return adminName;
        }

        public void setAdminName(String adminName) {
            this.adminName = adminName;
        }

        public long getAdminId() {
            return adminId;
        }

        public void setAdminId(long adminId) {
            this.adminId = adminId;
        }
    }
}
