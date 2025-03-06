package com.ecoapi.goodshopping.controller;


import com.ecoapi.goodshopping.request.LoginRequest;
import com.ecoapi.goodshopping.response.ApiResponse;
import com.ecoapi.goodshopping.response.JwtResponse;
import com.ecoapi.goodshopping.security.jwt.JwtUtils;
import com.ecoapi.goodshopping.security.user.ShopUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    private static Logger logger = LoggerFactory.getLogger(AuthController.class);

    // handles user login requests, authenticates users, and generates JWT tokens for authenticated users
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        logger.info("Login request received - Email: {}, Password: {}", request.getEmail(), request.getPassword());
        try {
            // Authenticate the User
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            // Sets the authenticated user’s details in the SecurityContextHolder, which makes the user authenticated for the current request
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Generate a JWT token for the authenticated user
            String jwt = jwtUtils.generateTokenForUser(authentication);
            // Retrieves the authenticated user’s details from the Authentication object
            ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();
            JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwt); // JwtResponse object containing the user’s ID and the generated JWT token
            return ResponseEntity.ok(new ApiResponse("Login Successful", jwtResponse));
        } catch (AuthenticationException e) { // If authentication fails (e.g., invalid email or password), an HTTP 401 (Unauthorized) response is returned with an error message
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(new ApiResponse(e.getMessage(), null));
        }
    }
}