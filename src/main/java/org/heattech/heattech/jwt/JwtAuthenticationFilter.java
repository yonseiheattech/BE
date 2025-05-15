package org.heattech.heattech.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.heattech.heattech.domain.member.domain.Role;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.userdetails.User;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil =jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtUtil.extractAccessTokenFromCookie(request);

        if (accessToken != null && jwtUtil.validateToken(accessToken)) {
            Long userId = jwtUtil.getUserIdFromToken(accessToken);
            String username = jwtUtil.getUsernameFromToken(accessToken);
            String roleString = jwtUtil.getRoleFromToken(accessToken);
            Role role = Role.valueOf(roleString);
            authenticateUser(userId, username, role); //role도 여기서 넣음
        } else {
            String refreshToken = jwtUtil.extractRefreshTokenFromCookie(request);

            if (refreshToken != null && jwtUtil.validateToken(refreshToken)) {
                Long userId = jwtUtil.getUserIdFromToken(refreshToken);
                String username = jwtUtil.getUsernameFromToken(refreshToken);
                String roleString = jwtUtil.getRoleFromToken(refreshToken);

                // access token 재발급
                String newAccessToken = jwtUtil.generateAccessToken(userId, username, roleString);
                ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", newAccessToken)
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .sameSite("None")
                        .maxAge(60 * 60)
                        .build();

                response.addHeader("Set-Cookie", accessTokenCookie.toString());

                // SecurityContext 등록
                Role role = Role.valueOf(roleString);
                authenticateUser(userId, username, role);

            }

        }
        filterChain.doFilter(request, response);    
    }

    private void authenticateUser(Long userId, String username, Role role) {
        CustomUserDetails userDetails = new CustomUserDetails(userId, username, role);


        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
