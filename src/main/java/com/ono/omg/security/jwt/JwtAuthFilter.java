package com.ono.omg.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.getHeaderToken(request, "Access");
        String refreshToken = jwtUtil.getHeaderToken(request, "Refresh");

        if(accessToken != null) {
            if(!jwtUtil.tokenValidation(accessToken)){
                log.info("====== JwtAuthFilter.doFilterInternal.accessToken.tokenValidation == false =====");
                jwtExceptionHandler(response, "AccessToken Expired", HttpStatus.UNAUTHORIZED);
                return;
            }
            setAuthentication(jwtUtil.getUsernameFromToken(accessToken));
        }else if(refreshToken != null) {
            if(!jwtUtil.refreshTokenValidation(refreshToken)){
                log.info("====== JwtAuthFilter.doFilterInternal.refreshToken.tokenValidation == false =====");
                jwtExceptionHandler(response, "RefreshToken Expired", HttpStatus.UNAUTHORIZED);
                return;
            }
            setAuthentication(jwtUtil.getUsernameFromToken(refreshToken));
        }

        filterChain.doFilter(request,response);
    }

    public void setAuthentication(String username) {
        Authentication authentication = jwtUtil.createAuthentication(username);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void jwtExceptionHandler(HttpServletResponse response, String msg, HttpStatus status) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(ResponseEntity.status(HttpStatus.UNAUTHORIZED));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
