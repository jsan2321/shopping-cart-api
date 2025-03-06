package com.ecoapi.goodshopping.data;

import com.ecoapi.goodshopping.model.Role;
import com.ecoapi.goodshopping.model.User;
import com.ecoapi.goodshopping.repository.RoleRepository;
import com.ecoapi.goodshopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private static Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles =  Set.of("ROLE_ADMIN", "ROLE_USER");
        createDefaultRoleIfNotExits(defaultRoles);
        createDefaultUserIfNotExits();
        createDefaultAdminIfNotExits();
    }

    private void createDefaultUserIfNotExits() {

        Role userRole = roleRepository.findByName("ROLE_USER")
                                      .orElseThrow(() -> new RuntimeException("ROLE_USER does not exist in the database."));

        for (int i = 1; i<=5; i++){
            String defaultEmail = "user"+i+"@email.com";
            if (userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            User user = new User();
            user.setFirstName("The User");
            user.setLastName("User" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
            logger.info("Default admin user {} created successfully.", i);

        }
    }

    private void createDefaultAdminIfNotExits(){
        Optional<Role> adminRoleOptional = roleRepository.findByName("ROLE_ADMIN");
        if (adminRoleOptional.isPresent()) {
            Role adminRole = adminRoleOptional.get();
            for (int i = 1; i<=2; i++){
                String defaultEmail = "admin"+i+"@email.com";
                if (userRepository.existsByEmail(defaultEmail)){
                    continue;
                }
                User user = new User();
                user.setFirstName("Admin");
                user.setLastName("Admin" + i);
                user.setEmail(defaultEmail);
                user.setPassword(passwordEncoder.encode("123456"));
                user.setRoles(Set.of(adminRole));
                userRepository.save(user);
                System.out.println("Default admin user " + i + " created successfully.");
            }
        } else {
            logger.error("ROLE_ADMIN does not exist in the database. Please create it first.");
        }
    }
    private void createDefaultRoleIfNotExits(Set<String> roles){
        roles.stream()
             .filter(role -> roleRepository.findByName(role).isEmpty())
             .map(Role::new)
             .forEach(roleRepository::save);

    }
}
