package org.heattech.heattech.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component //이거하면 new 안해도 됨
@Slf4j //log 관련
public class JwtUtil {

    @Value("${JWT_SECRET}")
    private String secret; // 256bit 이상!

    private Key key;

    @Value("${JWT_ACCESS_EXPIRE_MS}")
    private long accessExpirationTime;

    @Value("${JWT_REFRESH_EXPIRE_MS}")
    private long refreshExpirationTime;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Long id, String username, String role) {
        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("username", username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessExpirationTime))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(Long id, String username, String role) {
        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("username", username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationTime ))
                .signWith(key)
                .compact();
    }

    public String extractAccessTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null ) return null;

        for (Cookie cookie : request.getCookies()) {
            if ("accessToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    public String extractRefreshTokenFromCookie(HttpServletRequest request) {
        if(request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);//여기서 오류 없으면 유효한 토큰

            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("잘못된 서명");
        } catch (ExpiredJwtException e) {
            log.warn("만료된 토큰");
        } catch (UnsupportedJwtException e) {
            log.warn("우리가 지원하는 Jwt가 아님");
        } catch (IllegalArgumentException e) {
            log.warn("jwt 비어있음");
        }

        return false;
    }

    public boolean isTokenExpired(String token) {
        try {
            Date expiration = Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();

            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public Long getUserIdFromToken(String token) {
        return Long.valueOf(
                Jwts.parser()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject()
        );
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("username", String.class); //claim에서 꺼냄

    }

    public String getRoleFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
}
