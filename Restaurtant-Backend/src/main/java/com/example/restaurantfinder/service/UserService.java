package com.example.restaurantfinder.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.restaurantfinder.exception.ResourceNotFoundException;
import com.example.restaurantfinder.model.User;
import com.example.restaurantfinder.repository.UserRepository;
import com.example.restaurantfinder.security.JwtUtils;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    private static final String ADMIN_REGISTRATION_CODE = "@12TYIE";

    /**
     * Register a new Regular User
     */
    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already taken.");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already in use.");
        }
        user.setRole(User.Role.RegularUser);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Register a new Business Owner
     */
    public User registerBusinessOwner(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already taken.");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already in use.");
        }
        user.setRole(User.Role.BusinessOwner);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Authenticate Business Owner and generate JWT token
     */
    public String authenticateBusinessOwner(User user) {
        User foundUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Business Owner not found"));

        if (foundUser.getRole() != User.Role.BusinessOwner) {
            throw new RuntimeException("Access denied: Not a Business Owner");
        }

        if (passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            return jwtUtils.generateJwtToken(foundUser.getId(),foundUser.getUsername(), foundUser.getRole().name());
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    /**
     * Register a new Admin
     */
    public User registerAdmin(User user, String code) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already taken.");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already in use.");
        }
        if (!ADMIN_REGISTRATION_CODE.equals(code)) {
            throw new RuntimeException("Invalid registration code");
        }

        user.setRole(User.Role.Admin);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Authenticate Admin and generate JWT token
     */
    public String authenticateAdmin(User user) {
        User foundUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));

        if (foundUser.getRole() != User.Role.Admin) {
            throw new RuntimeException("Access denied: Not an Admin");
        }

        if (passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            return jwtUtils.generateJwtToken(foundUser.getId(),foundUser.getUsername(), foundUser.getRole().name());
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    /**
     * Authenticate a Regular User and generate JWT token
     */
    public String authenticateUser(User user) {
        User foundUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            return jwtUtils.generateJwtToken(foundUser.getId(),foundUser.getUsername(), foundUser.getRole().name());
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    // Other CRUD methods
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        user.setEmail(userDetails.getEmail());
        user.setUsername(userDetails.getUsername());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
