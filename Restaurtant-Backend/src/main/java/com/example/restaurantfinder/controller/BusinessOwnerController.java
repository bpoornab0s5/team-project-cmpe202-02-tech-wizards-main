package com.example.restaurantfinder.controller;

import java.util.List;

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
import org.springframework.web.multipart.MultipartFile;

import com.example.restaurantfinder.dto.RestaurantDto;
import com.example.restaurantfinder.dto.UserDto;
import com.example.restaurantfinder.model.User;
import com.example.restaurantfinder.service.BusinessOwnerService;
import com.example.restaurantfinder.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/business-owner")
public class BusinessOwnerController {

    @Autowired
    private BusinessOwnerService businessOwnerService;

    @Autowired
    private UserService userService;

    // Add a new restaurant listing
    // @PreAuthorize("hasRole('BUSINESS_OWNER')")
    @PostMapping("/restaurants")
    public ResponseEntity<RestaurantDto> addRestaurant(@RequestBody RestaurantDto restaurantDto) {
        RestaurantDto createdRestaurant = businessOwnerService.addRestaurant(restaurantDto);
        return ResponseEntity.ok(createdRestaurant);
    }

    // Update an existing restaurant
//    @PreAuthorize("hasRole('BUSINESS_OWNER')")
    @PutMapping("/restaurants/{id}")
    public ResponseEntity<RestaurantDto> updateRestaurant(@PathVariable Long id, @RequestBody RestaurantDto restaurantDto) {
        RestaurantDto updatedRestaurant = businessOwnerService.updateRestaurant(id, restaurantDto);
        return ResponseEntity.ok(updatedRestaurant);
    }

    // Delete a restaurant listing
//    @PreAuthorize("hasRole('BUSINESS_OWNER')")
    @DeleteMapping("/restaurants/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        businessOwnerService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }

    // View all listings owned by a specific BusinessOwner
//    @PreAuthorize("hasRole('BUSINESS_OWNER')")
    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantDto>> getRestaurantsByOwner(@Valid @RequestParam Long ownerId) {
        List<RestaurantDto> restaurants = businessOwnerService.getRestaurantsByOwner(ownerId);
        return ResponseEntity.ok(restaurants);
    }

    // NEW CODE
    // Register Business Owner
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerBusinessOwner(@Valid @RequestBody User user) {
        User registeredOwner = userService.registerBusinessOwner(user);
        return ResponseEntity.ok(convertToDto(registeredOwner));
    }

    // Login Business Owner
    @PostMapping("/login")
    public ResponseEntity<String> loginBusinessOwner(@Valid @RequestBody User user) {
        String message = userService.authenticateBusinessOwner(user);
        return ResponseEntity.ok(message);
    }

    private UserDto convertToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole() != null ? user.getRole().toString() : null
        );

    }

    @PostMapping("/restaurants/{id}/photos")
    public ResponseEntity<String> uploadPhoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        String photoUrl = businessOwnerService.uploadPhoto(id, file);
        return ResponseEntity.ok("Photo uploaded successfully: " + photoUrl);
    }

    @PutMapping("/restaurants/{id}/photos")
    public ResponseEntity<String> updatePhoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        String photoUrl = businessOwnerService.updatePhoto(id, file);
        return ResponseEntity.ok("Photo updated successfully: " + photoUrl);
    }

}
