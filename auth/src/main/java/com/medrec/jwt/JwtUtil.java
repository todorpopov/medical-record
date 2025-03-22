package com.medrec.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.util.Date;
import java.util.logging.Logger;

public class JwtUtil {
    private static JwtUtil instance;

    private final Logger logger = Logger.getLogger(JwtUtil.class.getName());

    private final String SECRET = "this is the JWT secret, which should be stored secretly";
    private final int TOKEN_VALIDITY = 1000 * 60 * 15; // 15 Minutes

    private JwtUtil() {}

    public static JwtUtil getInstance() {
        if (instance == null) {
            instance = new JwtUtil();
        }
        return instance;
    }

    public String generateToken(String email, String role) {
        long now = System.currentTimeMillis();

        return JWT.create()
            .withSubject(email)
            .withClaim("role", role)
            .withIssuedAt(new Date(now))
            .withExpiresAt(new Date(now + TOKEN_VALIDITY))
            .sign(Algorithm.HMAC256(SECRET));
    }

    public boolean isTokenValid(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            this.logger.severe(e.getMessage());
            return false;
        }
    }
}
