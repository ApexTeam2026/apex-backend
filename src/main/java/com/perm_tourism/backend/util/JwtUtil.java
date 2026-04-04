package com.perm_tourism.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    // Секретный ключ для подписи токенов
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Время жизни токена
    private static final long EXPIRATION_TIME = 86400000;

    // Создание токена на основе email
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    // Доставка email из токена
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // Проверка, не истёк ли срок действия токена
    public boolean isTokenValid(String token) {
        return getClaims(token).getExpiration().after(new Date());
    }

    // Извлечение всех данных из токена
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
