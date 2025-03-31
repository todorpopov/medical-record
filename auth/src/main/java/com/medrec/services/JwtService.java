package com.medrec.services;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class JwtService {
    private static JwtService instance;

    private final Logger logger = Logger.getLogger(JwtService.class.getName());

    private final String secret = System.getenv("JWT_SECRET");
    private final Key key = Keys.hmacShaKeyFor(secret.getBytes());
    private final long tokenValidityMs = 1000 * 60 * 60;

    private JwtService() {}

    public static JwtService getInstance() {
        if (instance == null) {
            instance = new JwtService();
        }
        return instance;
    }

    public String generateToken(String email, String role) {
        logger.info(String.format("Generating JWT token for email: %s and role: %s", email, role));

        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityMs);

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
            .setSubject(email)
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public boolean isUserAuthorized(String token, List<String> requiredRoles) {
        if(!isTokenValid(token)) {
            return false;
        }
        String tokenRole = getRole(token);

        for(String requiredRole : requiredRoles) {
            if(requiredRole.equals(tokenRole)) {
                return true;
            }
        }

        return false;
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            this.logger.severe(e.getMessage());
            return false;
        }
    }

    public String getEmail(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    public String getRole(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("role", String.class);
    }
}
