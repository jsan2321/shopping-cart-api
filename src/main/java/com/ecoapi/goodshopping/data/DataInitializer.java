package com.ecoapi.goodshopping.data;

import com.ecoapi.goodshopping.model.User;
import com.ecoapi.goodshopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.internal.util.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;

    //Logger logger = Logger.getLogger(this.getClass().getName());
    //Log log = (Log) LogFactory.getLogger(DataInitializer.class);

    private static Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        createDefaultUserIfNotExits();
    }

    private void createDefaultUserIfNotExits() {
        for (int i = 1; i<=5; i++){
            String defaultEmail = "user"+i+"@email.com";
            if (userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            User user = new User();
            user.setFirstName("The User");
            user.setLastName("User" + i);
            user.setEmail(defaultEmail);
            user.setPassword("123456");
            userRepository.save(user);
            logger.info("Default vet user \" + i + \" created successfully.");
            //System.out.println("Default vet user " + i + " created successfully.");
        }
    }
}
