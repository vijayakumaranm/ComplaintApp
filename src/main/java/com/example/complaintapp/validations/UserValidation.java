package com.example.complaintapp.validations;

import com.example.complaintapp.entity.User;
import com.example.complaintapp.exception.BadRequestException;
import com.example.complaintapp.exception.DuplicateResourceException;
import com.example.complaintapp.exception.InvalidCredentialsException;
import com.example.complaintapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserValidation {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void validateRegistration(User user) {
        if (user.getName() == null || !user.getName().matches("[a-zA-Z ]+")) {
            throw new BadRequestException("Name must contain alphabets only");
        }

        if (user.getEmail() == null || !user.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new BadRequestException("Invalid email format");
        }

        if (user.getPassword() == null || !user.getPassword().matches("^(?=.*[a-zA-Z])(?=.*[0-9]).{6,}$")) {
            throw new BadRequestException("Password must be at least 6 characters and alphanumeric");
        }

        if (user.getMobile() == null || !user.getMobile().matches("\\d{10}")) {
            throw new BadRequestException("Mobile must be a 10-digit number");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email already registered");
        }
    }

    public User validateLogin(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            throw new BadRequestException("Email is required");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new BadRequestException("Password is required");
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty() || !passwordEncoder.matches(password, optionalUser.get().getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return optionalUser.get();
    }
}