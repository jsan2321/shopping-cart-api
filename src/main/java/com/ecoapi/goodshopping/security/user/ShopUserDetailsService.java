package com.ecoapi.goodshopping.security.user;

import com.ecoapi.goodshopping.model.User;
import com.ecoapi.goodshopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopUserDetailsService implements UserDetailsService { // A custom implementation of UserDetailsService to load user-specific data (e.g., from a database) during authentication
    private final UserRepository userRepository;

    // loads user details by their username (in this case, email)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("HELLO ----- Email: " + email);
        User user = Optional.ofNullable(userRepository.findByEmail(email))
                            .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        return ShopUserDetails.buildUserDetails(user);
    }
}