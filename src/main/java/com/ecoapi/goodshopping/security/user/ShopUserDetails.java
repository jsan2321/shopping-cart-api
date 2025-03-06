package com.ecoapi.goodshopping.security.user;

import com.ecoapi.goodshopping.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShopUserDetails implements UserDetails { // This class implements the UserDetails interface, which represents the core user information required by Spring Security for authentication and authorization
    private Long id;
    private String email;
    private String password;

    private Collection<GrantedAuthority> authorities; // A collection of GrantedAuthority objects representing the user’s roles/permissions for authorization

    // convert a User entity into a ShopUserDetails object
    public static ShopUserDetails buildUserDetails(User user) {
        // Converts the user’s roles into GrantedAuthority objects (e.g., ROLE_ADMIN, ROLE_USER)
        List<GrantedAuthority> authorities = user.getRoles()
                                                 .stream()
                                                 .map(role -> new SimpleGrantedAuthority(role.getName()))
                                                 .collect(Collectors.toList());

        // Constructs and returns a ShopUserDetails object with the user’s details
        return new ShopUserDetails(user.getId(), user.getEmail(), user.getPassword(), authorities);
    }

    /*
    methods required by the UserDetails interface
    */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // retrieves and returns the encoded password from the database
    @Override
    public String getPassword() {
        return password;
    }

    // returns the user’s email (used as the username)
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    // returns true if the user’s credentials (password) are not expired
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
