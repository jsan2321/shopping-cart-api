package com.ecoapi.goodshopping.security.config;

import com.ecoapi.goodshopping.security.jwt.AuthTokenFilter;
import com.ecoapi.goodshopping.security.jwt.JwtAuthEntryPoint;
import com.ecoapi.goodshopping.security.user.ShopUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@RequiredArgsConstructor
@EnableWebSecurity // Enables Spring Security's web security support and provides the Spring MVC integration
@Configuration
@EnableMethodSecurity(prePostEnabled = true) // Enables method-level security using annotations like @PreAuthorize and @PostAuthorize
public class ShopConfig {
    private final ShopUserDetailsService userDetailsService;
    private final JwtAuthEntryPoint authEntryPoint;

    // List of URL patterns that require authentication
    private static final List<String> SECURED_URLS = List.of("/api/v1/carts/**", "/api/v1/cartItems/**");

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean // Bean for encoding passwords securely
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // A custom AuthTokenFilter bean for processing JWT tokens in incoming requests
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean // Retrieves the AuthenticationManager from the AuthenticationConfiguration to handle authentication
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean // Configures a DaoAuthenticationProvider to use the custom userDetailsService and passwordEncoder for authentication
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean // Configures the security filter chain for HTTP requests. It defines how authentication, authorization, and other security-related features work
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // Disables CSRF protection (common in stateless APIs using JWT)
            .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint)) // Sets the custom JwtAuthEntryPoint to handle authentication errors
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Configures the application to be stateless (no sessions)
            .authorizeHttpRequests(auth -> auth.requestMatchers(SECURED_URLS.toArray(String[]::new)) // converts the List<String> into a String[] (array of strings)
                                               .authenticated() // Only authenticated users (users who have logged in and provided valid credentials) can access these URLs
                                               .anyRequest().permitAll()); // all other requests (not matching the secured URLs) are allowed without authentication
        http.authenticationProvider(daoAuthenticationProvider()); // Registers the provider... Spring Security will still use the default DaoAuthenticationProvider because it detects the UserDetailsService and PasswordEncoder beans
        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class); // Adds the custom Filter before the default Filter to process JWT tokens
        return http.build(); // Finalizes and returns the configured SecurityFilterChain
    }
}