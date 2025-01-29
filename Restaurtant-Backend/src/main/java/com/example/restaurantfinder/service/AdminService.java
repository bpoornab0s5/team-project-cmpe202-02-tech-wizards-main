package com.example.restaurantfinder.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restaurantfinder.dto.LocationDto;
import com.example.restaurantfinder.dto.RestaurantDto;
import com.example.restaurantfinder.exception.ResourceNotFoundException;
import com.example.restaurantfinder.model.Restaurant;
import com.example.restaurantfinder.model.User;
import com.example.restaurantfinder.repository.RestaurantRepository;
import com.example.restaurantfinder.repository.UserRepository;

@Service
public class AdminService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    // public List<Restaurant> getDuplicateRestaurants() {
    //     // Implement logic to find duplicate restaurants
    //     return restaurantRepository.findAll(); // Placeholder
    // }
    public void deleteRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id " + id));
        restaurantRepository.delete(restaurant);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        userRepository.delete(user);
    }

    public List<Restaurant> getDuplicateRestaurants() {
        // Repository-based duplicates
        List<Restaurant> duplicatesFromQuery = restaurantRepository.findDuplicateRestaurants();

        // Stream-based duplicates
        List<Restaurant> allRestaurants = restaurantRepository.findAll();
        List<Restaurant> duplicatesFromStream = allRestaurants.stream()
                .filter(restaurant -> {
                    long count = allRestaurants.stream()
                            .filter(other -> restaurant.getName().equalsIgnoreCase(other.getName())
                                    && restaurant.getAddress().equalsIgnoreCase(other.getAddress()))
                            .count();
                    return count > 1; // If more than one restaurant has the same name and address
                })
                .distinct()
                .collect(Collectors.toList());

        // Combine both results and remove duplicates
        return duplicatesFromStream.stream()
                .filter(duplicatesFromQuery::contains) // Keep only the overlap between both results
                .distinct()
                .collect(Collectors.toList());
    }

    public List<RestaurantDto> convertRestaurantsToDtoWithoutReviews(List<Restaurant> restaurants) {
        return restaurants.stream()
            .map(this::convertToDtoWithoutReviews)
            .collect(Collectors.toList());
    }

        // Helper method to convert Restaurant to RestaurantDto without reviews
        private RestaurantDto convertToDtoWithoutReviews(Restaurant restaurant) {
            return new RestaurantDto(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getContactInfo(),
                restaurant.getHours(),
                restaurant.getCuisine(),
                restaurant.getFoodType(),
                restaurant.getPriceRange(),
                restaurant.getRating(),
                restaurant.getDescription(),
                restaurant.getPhotos(),
                restaurant.getLocation() != null
                    ? new LocationDto(restaurant.getLocation().getLatitude(), restaurant.getLocation().getLongitude())
                    : null,
                restaurant.getCity(),
                restaurant.getZipCode(),
                restaurant.getState(),
                restaurant.getCountry(),
                restaurant.getOwner() != null ? restaurant.getOwner().getId() : null, false
            );
        }

    @Autowired
    private RestaurantService restaurantService;
    
    public void markRestaurantAsOpen(Long restaurantId) {
            restaurantService.markRestaurantAsOpen(restaurantId);
    }
        
    public void deleteReportedRestaurant(Long restaurantId) {
            restaurantService.deleteReportedRestaurant(restaurantId);
        }

    public AdminService(RestaurantRepository restaurantRepository, UserRepository userRepository,
            RestaurantService restaurantService) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.restaurantService = restaurantService;
    }
        
}
