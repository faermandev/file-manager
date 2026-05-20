package com.faermandev.file_manager.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY = "my-super-secret-key-my-super-secret-key";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    private SecretKey getSigninKey() {
        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
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
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

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