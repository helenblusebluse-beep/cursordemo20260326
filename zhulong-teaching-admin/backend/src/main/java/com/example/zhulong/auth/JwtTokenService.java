package com.example.zhulong.auth;

import com.example.zhulong.config.ZhulongProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtTokenService {

    private final ZhulongProperties zhulongProperties;
    private final SecretKey key;

    public JwtTokenService(ZhulongProperties zhulongProperties) {
        this.zhulongProperties = zhulongProperties;
        byte[] bytes = zhulongProperties.getJwt().getSecret().getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(long userId, String username, String displayName) {
        long hours = Math.max(1, zhulongProperties.getJwt().getExpireHours());
        long expireMs = hours * 3600_000L;
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("name", displayName)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expireMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
