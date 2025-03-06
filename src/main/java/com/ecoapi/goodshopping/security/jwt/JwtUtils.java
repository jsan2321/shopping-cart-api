package com.ecoapi.goodshopping.security.jwt;

import com.ecoapi.goodshopping.security.user.ShopUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils { // A utility class for JWT operations (e.g., validating tokens, extracting usernames)

    @Value("${auth.token.jwtSecret}") // Injects values from the application configuration
    private String jwtSecret;

    @Value("${auth.token.expirationInMils}")
    private int expirationTime;

    public String generateTokenForUser(Authentication authentication) {

        ShopUserDetails userPrincipal = (ShopUserDetails) authentication.getPrincipal();

        List<String> roles = userPrincipal.getAuthorities()
                                          .stream()
                                          .map(GrantedAuthority::getAuthority)
                                          .toList();

        // Build JWT Token
        return Jwts.builder()
                   .subject(userPrincipal.getEmail())
                   .claim("id", userPrincipal.getId())
                   .claim("roles", roles)
                   .issuedAt(new Date())
                   .expiration(new Date((new Date()).getTime() + expirationTime))
                   .signWith(key())
                   .compact();
    }

    // generates a signing key from the base64-encoded jwtSecret
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // extracts the username (subject) from a parsed JWT token
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                   //.setSigningKey(key())
                   .verifyWith( (SecretKey) key())
                   .build()
                   //.parseClaimsJws(token)
                   .parseSignedClaims(token)
                   //.getBody()
                   .getPayload()
                   .getSubject();
    }

    public  boolean validateToken(String token) {
        try {
            Jwts.parser()
                //.setSigningKey(key())
                .verifyWith( (SecretKey) key())
                .build()
                //.parseClaimsJws(token); // parse the token
                .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new JwtException(e.getMessage());
        }
    }
}
