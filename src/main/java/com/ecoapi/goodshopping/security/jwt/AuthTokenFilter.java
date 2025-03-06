package com.ecoapi.goodshopping.security.jwt;

import com.ecoapi.goodshopping.security.user.ShopUserDetailsService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// custom Spring Security filter that processes incoming HTTP requests to validate JWT tokens and set up the authentication context for the request
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private ShopUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            // Extract the JWT token from the (Authorization header) request
            String jwt = parseJwt(request);
            // check if token is not empty and is valid
            if (StringUtils.hasText(jwt) && jwtUtils.validateToken(jwt)) {
                String username = jwtUtils.getUsernameFromToken(jwt); // Extracts the username (or subject) from the JWT token
                UserDetails userDetails = userDetailsService.loadUserByUsername(username); // load user details (e.g., roles, authorities) based on the username
                // Set up the authentication context
                var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(e.getMessage() +" : Invalid or expired token, you may login and try again!");
            return;
        }catch (Exception e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(e.getMessage());
            return;
        }
        filterChain.doFilter(request, response); // If no errors occur, the request is passed to the next filter in the chain
    }

    private String parseJwt(HttpServletRequest request) {
        // Get Authorization Header
        String headerAuth = request.getHeader("Authorization");
        // Checks if the header is not empty and starts with "Bearer "
        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            // Extracts the token by removing the "Bearer " prefix
            return headerAuth.substring(7);
        }
        return  null; // Returns null if the header is invalid or missing
    }
}
