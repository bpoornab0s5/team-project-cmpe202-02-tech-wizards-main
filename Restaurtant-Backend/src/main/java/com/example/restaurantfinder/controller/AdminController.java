package com.example.restaurantfinder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restaurantfinder.dto.RestaurantDto;
import com.example.restaurantfinder.model.Restaurant;
import com.example.restaurantfinder.model.User;
import com.example.restaurantfinder.service.AdminService;

@RestController
@RequestMapping("/api/admin")
    public class AdminController {

    @Autowired
    private AdminService adminService;

    // New endpoint to get duplicate restaurants
    // @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/duplicates")
    public ResponseEntity<List<RestaurantDto>> getDuplicateRestaurants() {
        List<Restaurant> duplicateRestaurants = adminService.getDuplicateRestaurants();
        List<RestaurantDto> duplicateDtos = adminService.convertRestaurantsToDtoWithoutReviews(duplicateRestaurants);
        return ResponseEntity.ok(duplicateDtos);
    }
    

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/restaurants/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        adminService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    // Admin endpoint to mark a restaurant as open
    // @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/restaurants/{id}/mark-open")
    public ResponseEntity<String> markRestaurantAsOpen(@PathVariable Long id) {
        adminService.markRestaurantAsOpen(id);
        return ResponseEntity.ok("Restaurant marked as open");
    }

    // Admin endpoint to delete a reported restaurant
    // @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/restaurants/{id}/delete-reported")
    public ResponseEntity<String> deleteReportedRestaurant(@PathVariable Long id) {
        adminService.deleteReportedRestaurant(id);
        return ResponseEntity.ok("Reported restaurant deleted successfully");
    }

}
  