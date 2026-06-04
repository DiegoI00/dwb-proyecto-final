package com.invoice.config.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final String SECRET =
            "mi_clave_super_secreta_para_jwt_segura_2026_abcdef";

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public Claims extractClaims(String token) {

        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();

        return jwtParser.parseClaimsJws(token).getBody();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @SuppressWarnings("unchecked")
    public List<String> extractPermisos(String token) {
        Object rolesClaim = extractClaims(token).get("roles");

        if (!(rolesClaim instanceof List<?> roles)) {
            return List.of();
        }

        return roles.stream()
                .map(role -> {
                    if (role instanceof String authority) {
                        return authority;
                    }

                    if (role instanceof java.util.Map<?, ?> roleMap) {
                        Object authority = roleMap.get("authority");
                        if (authority instanceof String authorityValue) {
                            return authorityValue;
                        }
                    }

                    return null;
                })
                .filter(java.util.Objects::nonNull)
                .toList();
    }

    public boolean isTokenValid(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractClaims(token));
    }

    public Integer extractUserId(String token) {
        return Integer.parseInt(extractClaims(token).get("id").toString());
    }
}
