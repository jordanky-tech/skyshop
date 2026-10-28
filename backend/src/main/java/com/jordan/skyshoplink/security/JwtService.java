package com.jordan.skyshoplink.security;

import com.jordan.skyshoplink.entity.User;
import com.jordan.skyshoplink.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    private final String ENCRYPTION;
    private final UserService userService;

    public JwtService(@Value("${app.jwt.access.secret}") String ENCRYPTION, UserService userService) {
        this.ENCRYPTION = ENCRYPTION;
        this.userService = userService;
    }

    public Map<String, String> generate(String email) {
        User user = this.userService.loadUserByUsername(email);
        return this.generateJwt(user);
    }

    private Map<String, String> generateJwt(User user) {
        Map<String, String> claims = Map.of("username", user.getEmail(), "email", user.getEmail());
        long current = System.currentTimeMillis();
        long expiration = current + 30L * 60 * 1000;

        String bearer = Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(new Date(current))
                .expiration(new Date(expiration))
                .signWith(this.getKey())
                .compact();
        return Map.of("bearer", bearer);
    }

    public SecretKey getKey() {
        byte[] decode = Decoders.BASE64.decode(ENCRYPTION);
        return Keys.hmacShaKeyFor(decode);
    }

    public String extractUsername(String token) {
        return this.getFromClaims(token, Claims::getSubject);
    }

    public boolean istokenExpired(String token) {
        Date expiration = this.getFromClaims(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    public <T> T getFromClaims(String token, Function<Claims, T> function) {
        Claims claims = this.getClaims(token);
        return function.apply(claims);
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
