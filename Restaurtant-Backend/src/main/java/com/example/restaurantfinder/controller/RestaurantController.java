package com.example.restaurantfinder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.restaurantfinder.dto.RestaurantDto;
import com.example.restaurantfinder.dto.RestaurantSearchResponse;
import com.example.restaurantfinder.service.OptionService;
import com.example.restaurantfinder.service.RestaurantService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private OptionService optionService;

    @GetMapping("/search")
    public ResponseEntity<RestaurantSearchResponse> searchRestaurants(@Valid
        @RequestParam(required = false) @Size(max = 5, message = "Zip code should be at most 5 digits") String zipCode,
        @RequestParam(required = false) String city,
        @RequestParam(required = false) String country,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String cuisine,
        @RequestParam(required = false) String priceRange,
        @RequestParam(required = false) Double minRating,
        @RequestParam(required = false) String foodType) {
    
        RestaurantSearchResponse response = restaurantService.searchRestaurantsUnifiedByFilters(
            zipCode, city, country, name, cuisine, priceRange, minRating, foodType
        );
        return ResponseEntity.ok(response);
    }
      
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> getRestaurantById(@PathVariable Long id) {
        RestaurantDto restaurant = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(restaurant);
    }

    @PreAuthorize("hasRole('BUSINESS_OWNER')")
    @PostMapping
    public ResponseEntity<RestaurantDto> addRestaurant(@Valid @RequestBody RestaurantDto restaurantDto) {
        RestaurantDto createdRestaurant = restaurantService.saveRestaurant(restaurantDto);
        return ResponseEntity.ok(createdRestaurant);
    }

    @PreAuthorize("hasRole('BUSINESS_OWNER')")
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDto> updateRestaurant(@Valid @PathVariable Long id, @RequestBody RestaurantDto restaurantDto) {
        RestaurantDto updatedRestaurant = restaurantService.updateRestaurant(id, restaurantDto);
        return ResponseEntity.ok(updatedRestaurant);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('BUSINESS_OWNER')")
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<RestaurantDto>> getRestaurantsByOwner(@PathVariable Long ownerId) {
        List<RestaurantDto> restaurants = restaurantService.getRestaurantsByOwner(ownerId);
        return ResponseEntity.ok(restaurants);
    }

    // @GetMapping("/zipcode/{zipCode}")
    // public ResponseEntity<List<RestaurantDto>> searchRestaurantsByZipCode(@PathVariable String zipCode) {
    //     List<RestaurantDto> restaurants = restaurantService.searchRestaurantsByZip(zipCode);
    //     return ResponseEntity.ok(restaurants);
    // }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        return ResponseEntity.ok(restaurantService.getRestaurantCategories());
    }

    @GetMapping("/food-types")
    public ResponseEntity<List<String>> getFoodTypes() {
        return ResponseEntity.ok(restaurantService.getFoodTypes());
    }

    @GetMapping("/price-ranges")
    public ResponseEntity<List<String>> getPriceRanges() {
        return ResponseEntity.ok(restaurantService.getPriceRanges());
    }

    @PostMapping("/{id}/report-closed")
    public ResponseEntity<String> reportClosedRestaurant(@PathVariable Long id) {
        restaurantService.reportClosedRestaurant(id);
        return ResponseEntity.ok("Restaurant reported as closed");
    }

    @GetMapping("/reported-closed")
    public ResponseEntity<List<RestaurantDto>> getReportedClosedRestaurants() {
        List<RestaurantDto> reportedRestaurants = restaurantService.getReportedClosedRestaurants();
        return ResponseEntity.ok(reportedRestaurants);
    }

    @PostMapping("/{id}/mark-open")
    public ResponseEntity<String> markRestaurantAsOpen(@PathVariable Long id) {
        restaurantService.markRestaurantAsOpen(id);
        return ResponseEntity.ok("Restaurant marked as open");
    }

    @DeleteMapping("/{id}/delete-reported")
    public ResponseEntity<String> deleteReportedRestaurant(@PathVariable Long id) {
        restaurantService.deleteReportedRestaurant(id);
        return ResponseEntity.ok("Reported restaurant deleted successfully");
    }

}
