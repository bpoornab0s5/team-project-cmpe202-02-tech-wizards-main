package com.example.restaurantfinder.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService userDetailsService;
@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
    String header = request.getHeader("Authorization"); // Extract the Authorization header
    String token = null;
    String username = null;

    if (header != null && header.startsWith("Bearer ")) {
        token = header.substring(7); // Extract the token (remove "Bearer ")
        try {
            // Validate and extract claims from the token
            Claims claims = jwtUtils.getClaimsFromToken(token);
            username = claims.getSubject();
            String role = claims.get("role", String.class); // Extract the role claim
            logger.info("Username: " + username + ", Role: " + role);
            
            // If the user is not already authenticated, set the authentication in the SecurityContext
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username, null, Collections.singletonList(new SimpleGrantedAuthority(role)) // Map role to authority
                );
                logger.info("Security Context Authentication: " + SecurityContextHolder.getContext().getAuthentication());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication", e); // Log any error during authentication
        }
    }
     
    // Continue with the filter chain
    filterChain.doFilter(request, response);
}



}
