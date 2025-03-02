package com.ecoapi.goodshopping.data;

import com.ecoapi.goodshopping.model.Role;
import com.ecoapi.goodshopping.model.User;
import com.ecoapi.goodshopping.repository.RoleRepository;
import com.ecoapi.goodshopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.internal.util.logging.Log;
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

    //Logger logger = Logger.getLogger(this.getClass().getName());
    //Log log = (Log) LogFactory.getLogger(DataInitializer.class);

    private static Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles =  Set.of("ROLE_ADMIN", "ROLE_USER");
        createDefaultRoleIfNotExits(defaultRoles); // Ensure roles are created first
        createDefaultUserIfNotExits();
        createDefaultAdminIfNotExits();
    }

    private void createDefaultUserIfNotExits() {
        // Returns an Optional<Role>
        // If no role with the given name exists in the database, the Optional will be empty. Calling .get() on an empty Optional throws a NoSuchElementException.
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        // Handle the case where the Optional is empty
        // 1. Check if the Role Exists Before Calling .get()
        /*
        Optional<Role> userRoleOptional = roleRepository.findByName("ROLE_USER");
        if (userRoleOptional.isPresent()) {
            Role userRole = userRoleOptional.get();
            // Proceed with creating users
        } else {
            logger.error("ROLE_USER does not exist in the database. Please create it first.");
            // Optionally, create the ROLE_USER role here
        }
         */
        // 2. Throw a custom exception if the role does not exist:
        //Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("ROLE_USER does not exist in the database."));
        // 3. Create the Role if It Doesn’t Exist
        /*
        Role userRole = roleRepository.findByName("ROLE_USER")
                                      .orElseGet(() -> {
                                          Role newRole = new Role("ROLE_USER");
                                          return roleRepository.save(newRole);
                                      });
        */
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
            //System.out.println("Default vet user " + i + " created successfully.");
        }
    }

    private void createDefaultAdminIfNotExits(){
        // Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
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
             .map(Role:: new).forEach(roleRepository::save);

    }
}
