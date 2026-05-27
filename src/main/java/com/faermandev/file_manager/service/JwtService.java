package com.faermandev.file_manager.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    private SecretKey getSigninKey() {
        return Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8)
        );
    }

    public boolean isTokenValid(String token, String userEmail) {
        String email = extractEmail(token);
        return email.equals(userEmail) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date now = new Date();
        Date expirationDate = Jwts.parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
        return now.after(expirationDate);
    }

    public String generateToken(String email) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationTime);

        SecretKey key = getSigninKey();

        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(key)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}