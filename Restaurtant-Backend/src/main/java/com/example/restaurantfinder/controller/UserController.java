package com.example.restaurantfinder.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.restaurantfinder.dto.ReviewDto;
import com.example.restaurantfinder.dto.UserDto;
import com.example.restaurantfinder.model.User;
import com.example.restaurantfinder.service.ReviewService;
import com.example.restaurantfinder.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/review/{userId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByUser(@PathVariable Long userId) {
        List<ReviewDto> reviews = reviewService.getReviewsByUser(userId);
        return ResponseEntity.ok(reviews);
    }


    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        UserDto userDto = convertToDto(registeredUser);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<UserDto> registerAdmin(@Valid @RequestBody User user, @RequestParam String code) {
        User registeredAdmin = userService.registerAdmin(user, code);
        UserDto userDto = convertToDto(registeredAdmin);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody User user) {
        String message = userService.authenticateUser(user);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/loginAdmin")
    public ResponseEntity<String> loginAdmin(@Valid @RequestBody User user) {
        String message = userService.authenticateAdmin(user);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        UserDto userDto = convertToDto(user);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        UserDto userDto = convertToDto(updatedUser);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDto> userDtos = users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    private UserDto convertToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole() != null ? user.getRole().toString() : null
        );
    }
}

