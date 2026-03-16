package com.example.complaintapp.service;

import com.example.complaintapp.entity.User;
import com.example.complaintapp.repository.UserRepository;
import com.example.complaintapp.validations.UserValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserValidation userValidation;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Map<String, Object> registerUser(User user) {
        logger.info("Register request received for email: {}", user.getEmail());

        userValidation.validateRegistration(user);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userRepository.save(user);

        logger.info("User registered successfully with email: {}", user.getEmail());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "User registered successfully");
        return response;
    }

    public Map<String, Object> loginUser(String email, String password) {
        logger.info("Login request received for email: {}", email);

        User user = userValidation.validateLogin(email, password);

        logger.info("Login successful for email: {}", email);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Login successful");
        response.put("userId", user.getId());
        response.put("name", user.getName());
        response.put("role", user.getRole());
        return response;
    }
}