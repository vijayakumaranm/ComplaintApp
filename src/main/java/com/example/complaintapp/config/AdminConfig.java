package com.example.complaintapp.config;

import com.example.complaintapp.entity.User;
import com.example.complaintapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminConfig implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AdminConfig.class);

    @Autowired
    private UserRepository userRepository;

    @Value("${root.email}")
    private String rootEmail;

    @Value("${root.password}")
    private String rootPassword;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail(rootEmail).isEmpty()) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail(rootEmail);
            admin.setPassword(passwordEncoder.encode(rootPassword));
            admin.setMobile("9566337351");
            admin.setRole("ADMIN");
            userRepository.save(admin);
            logger.info("Admin user created from properties");
        } else {
            logger.info("Admin user already exists, skipping creation");
        }
    }
}