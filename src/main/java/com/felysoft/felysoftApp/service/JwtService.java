package com.felysoft.felysoftApp.service;

import com.felysoft.felysoftApp.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    @Value("${security.jwt.expiration-minutes}")
    private long EXPIRATION_MINUTES;

    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;

    public String generateToken(User user) {
        // Crear claims con solo el rol
        Map<String, Object> extraClaims = Map.of(
                "role", user.getRole().getName()
        );

        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (EXPIRATION_MINUTES * 60 * 1000)))
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .signWith(generateKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String generateVerificationToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + (1440 * 60 * 1000))) // Tiempo de expiración 24h
                .signWith(generateKey(), Jwts.SIG.HS256)
                .compact();
    }

    public long getExpirationMillis() {
        return EXPIRATION_MINUTES * 60 * 1000;
    }

    private SecretKey generateKey() {
        byte[] secretAsBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(secretAsBytes);
    }

    public String extractUsername(String jwt) {
        return extractAllClaims(jwt).getSubject();
    }

    public String extractRole(String jwt) {
        return (String) extractAllClaims(jwt).get("role");
    }

    private Claims extractAllClaims(String jwt) {
        return Jwts
                .parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    public boolean isTokenValid(String jwt, User user) {
        final String username = extractUsername(jwt);
        final String role = extractRole(jwt);
        return (username.equals(user.getUsername()) && !isTokenExpired(jwt) && role.equals(user.getRole().getName()));
    }

    private boolean isTokenExpired(String jwt) {
        return extractAllClaims(jwt).getExpiration().before(new Date());
    }
}
