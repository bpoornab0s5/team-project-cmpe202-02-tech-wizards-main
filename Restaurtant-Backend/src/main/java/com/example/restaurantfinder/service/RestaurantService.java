package com.example.restaurantfinder.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.restaurantfinder.dto.LocationDto;
import com.example.restaurantfinder.dto.RestaurantDto;
import com.example.restaurantfinder.dto.RestaurantSearchResponse;
import com.example.restaurantfinder.exception.ResourceNotFoundException;
import com.example.restaurantfinder.model.Location;
import com.example.restaurantfinder.model.Restaurant;
import com.example.restaurantfinder.repository.RestaurantRepository;
// import lombok.Value;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private GeocodingService geocodingService;

    private static final String GOOGLE_PLACES_API_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";

    public RestaurantDto saveRestaurant(RestaurantDto restaurantDto) {
        Restaurant restaurant = convertToEntity(restaurantDto);
        restaurant = restaurantRepository.save(restaurant);
        return convertToDto(restaurant);
    }

    // public List<RestaurantDto> searchRestaurants(
    //         String zipCode, String name, String cuisine, String priceRange, Double minRating) {
    //     List<Restaurant> restaurants = restaurantRepository.findByFilters(zipCode, name, cuisine, priceRange, minRating);
    //     return restaurants.stream().map(this::convertToDto).collect(Collectors.toList());
    // }

    public RestaurantDto getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
        return convertToDto(restaurant);
    }

    public RestaurantDto updateRestaurant(Long id, RestaurantDto restaurantDto) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
        restaurant.setName(restaurantDto.getName());
        restaurant.setAddress(restaurantDto.getAddress());
        restaurant.setCuisine(restaurantDto.getCuisine());
        restaurant.setPriceRange(restaurantDto.getPriceRange());
        restaurant.setRating(restaurantDto.getRating());
        return convertToDto(restaurantRepository.save(restaurant));
    }

    public void deleteRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
        restaurantRepository.delete(restaurant);
    }

    public List<RestaurantDto> getRestaurantsByOwner(Long ownerId) {
        List<Restaurant> restaurants = restaurantRepository.findByOwnerId(ownerId);
        return restaurants.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    private String extractCity(String formattedAddress) {
        String[] addressParts = formattedAddress.split(",");
        if (addressParts.length >= 3) {
            return addressParts[addressParts.length - 3].trim();
        }
        return null;
    }

    private String extractState(String formattedAddress) {
        String[] addressParts = formattedAddress.split(",");
        if (addressParts.length >= 2) {
            String stateZip = addressParts[addressParts.length - 2].trim();
            return stateZip.split(" ")[0]; // Return only the state name
        }
        return null;
    }

// Update extractZipCode to properly handle zip extraction
private String extractZipCode(String formattedAddress) {
    String[] addressParts = formattedAddress.split(",");
    if (addressParts.length >= 2) {
        String stateZip = addressParts[addressParts.length - 2].trim();
        String[] stateZipParts = stateZip.split(" ");
        if (stateZipParts.length > 1) {
            return stateZipParts[1]; // Return the zip code
        }
    }
    return null;
}

    private String extractCountry(String formattedAddress) {
        String[] addressParts = formattedAddress.split(",");
        if (addressParts.length >= 1) {
            return addressParts[addressParts.length - 1].trim();
        }
        return null;
    }

    private String getPriceLevelDescription(int priceLevel) {
        switch (priceLevel) {
            case 0:
            case 1:
                return "$";
            case 2:
                return "$$";
            case 3:
                return "$$$";
            case 4:
                return "$$$$";
            default:
                return null;
        }
    }

    @Value("${google.maps.api.key}")
    private String apiKey;

    private List<RestaurantDto> fetchExternalRestaurants(LocationDto location) {
        RestTemplate restTemplate = new RestTemplate();
        String placesApiUrl = GOOGLE_PLACES_API_URL + "?location=" + location.getLatitude() + "," + location.getLongitude()
                + "&radius=5000&type=restaurant&key=" + apiKey;
    
        // Fetch nearby restaurants from Places API
        Map<String, Object> response = restTemplate.getForObject(placesApiUrl, Map.class);
        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
    
        if (results == null || results.isEmpty()) {
            return new ArrayList<>();
        }
    
        List<RestaurantDto> externalRestaurants = new ArrayList<>();
        for (var result : results) {
            String placeId = (String) result.get("place_id");
    
            // Call Place Details API to fetch detailed information
            String detailsApiUrl = "https://maps.googleapis.com/maps/api/place/details/json?place_id=" + placeId + "&key=" + apiKey;
            Map<String, Object> detailsResponse = restTemplate.getForObject(detailsApiUrl, Map.class);
    
            if (detailsResponse != null && "OK".equals(detailsResponse.get("status"))) {
                Map<String, Object> details = (Map<String, Object>) detailsResponse.get("result");
                RestaurantDto dto = new RestaurantDto();
    
                dto.setName((String) details.get("name"));
                dto.setAddress((String) details.get("formatted_address"));
                dto.setContactInfo((String) details.get("formatted_phone_number"));
                dto.setHours(details.containsKey("opening_hours") ? ((Map<String, Object>) details.get("opening_hours")).get("weekday_text").toString() : null);
                dto.setRating(details.get("rating") != null ? Double.parseDouble(details.get("rating").toString()) : null);
                dto.setDescription((String) details.get("vicinity"));
    
                // Populate location
                Map<String, Object> geometry = (Map<String, Object>) details.get("geometry");
                if (geometry != null) {
                    Map<String, Object> locationData = (Map<String, Object>) geometry.get("location");
                    dto.setLocation(new LocationDto(
                            Double.parseDouble(locationData.get("lat").toString()),
                            Double.parseDouble(locationData.get("lng").toString())
                    ));
                }
    
                // Extract and clean address details
                String formattedAddress = (String) details.get("formatted_address");
                if (formattedAddress != null) {
                    dto.setCity(extractCity(formattedAddress));
                    dto.setState(extractState(formattedAddress));
                    dto.setCountry(extractCountry(formattedAddress));
    
                    // Set ZipCode using the new method
                    dto.setZipCode(extractZipCode(formattedAddress));
                }
    
                // Set cuisine and food type with defaults if not provided
                dto.setCuisine(details.get("cuisine") != null ? (String) details.get("cuisine") : "General");
                dto.setFoodType(details.get("foodType") != null ? (String) details.get("foodType") : "Mixed");
    
                // Set price range
                dto.setPriceRange(details.get("price_level") != null ? getPriceLevelDescription((Integer) details.get("price_level")) : null);
    
                externalRestaurants.add(dto);
            }
        }
    
        return externalRestaurants;
    }
    
    
    private Restaurant convertToEntity(RestaurantDto dto) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(dto.getName());
        restaurant.setAddress(dto.getAddress());
        restaurant.setCuisine(dto.getCuisine());
        restaurant.setPriceRange(dto.getPriceRange());
        restaurant.setRating(dto.getRating());
        restaurant.setDescription(dto.getDescription());
        restaurant.setPhotos(dto.getPhotos());
        restaurant.setContactInfo(dto.getContactInfo());
        restaurant.setHours(dto.getHours());
        restaurant.setFoodType(dto.getFoodType());
        restaurant.setZipCode(dto.getZipCode());
        restaurant.setState(dto.getState());
        restaurant.setCountry(dto.getCountry());

        if (dto.getLocation() != null) {
            Location location = new Location();
            location.setLatitude(dto.getLocation().getLatitude());
            location.setLongitude(dto.getLocation().getLongitude());
            restaurant.setLocation(location);
        }
        return restaurant;
    }

    private RestaurantDto convertToDto(Restaurant restaurant) {
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

    public List<String> getRestaurantCategories() {
        return Arrays.asList("Greek", "Mexican", "Italian", "Chinese", "Thai", "Indian");
    }

    public List<String> getFoodTypes() {
        return Arrays.asList("Vegetarian", "Vegan", "Gluten-Free", "Non-Vegetarian");
    }

    public List<String> getPriceRanges() {
        return Arrays.asList("$", "$$", "$$$", "$$$$");
    }

    public void reportClosedRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id " + restaurantId));
        restaurant.setReportedClosed(true);
        restaurantRepository.save(restaurant);
    }

    public List<RestaurantDto> getReportedClosedRestaurants() {
        List<Restaurant> reportedRestaurants = restaurantRepository.findByIsReportedClosedTrue();
        return reportedRestaurants.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public void markRestaurantAsOpen(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id " + restaurantId));
        restaurant.setReportedClosed(false);
        restaurantRepository.save(restaurant);
    }

    public void deleteReportedRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id " + restaurantId));
        if (!restaurant.isReportedClosed()) {
            throw new RuntimeException("Restaurant is not reported as closed, cannot be deleted");
        }
        restaurantRepository.delete(restaurant);
    }

    public RestaurantRepository getRestaurantRepository() {
        return this.restaurantRepository;
    }

    public void setRestaurantRepository(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

//     public RestaurantSearchResponse searchRestaurantsUnifiedByFilters(
//         String zipCode, String city, String country, String name, String cuisine, String priceRange, Double minRating) {

//     // Fetch internal restaurants with precise filtering
//     List<RestaurantDto> internalRestaurants = restaurantRepository
//             .findByFilters(zipCode, name, cuisine, priceRange, minRating)
//             .stream()
//             .filter(restaurant -> 
//                 (city == null || restaurant.getCity() != null && city.equalsIgnoreCase(restaurant.getCity())) &&
//                 (zipCode == null || restaurant.getZipCode() != null && zipCode.equals(restaurant.getZipCode()))
//             )
//             .map(this::convertToDto)
//             .collect(Collectors.toList());

//     // Fetch external restaurants
//     List<RestaurantDto> externalRestaurants = new ArrayList<>();
//     if (zipCode != null || city != null || country != null) {
//         try {
//             LocationDto location = geocodingService.getCoordinates(zipCode, city, country);
//             externalRestaurants = fetchExternalRestaurants(location);
//         } catch (Exception e) {
//             System.err.println("Error fetching external restaurants: " + e.getMessage());
//         }
//     }

//     // Combine results into wrapper class
//     RestaurantSearchResponse response = new RestaurantSearchResponse();
//     response.setInternalRestaurants(internalRestaurants);
//     response.setExternalRestaurants(externalRestaurants);

//     return response;
// }

// }

public RestaurantSearchResponse searchRestaurantsUnifiedByFilters(
    String zipCode, String city, String country, String name, String cuisine, String priceRange, Double minRating, String foodType) {

    // Fetch internal restaurants with precise filtering
    List<RestaurantDto> internalRestaurants = restaurantRepository
            .findByFilters(zipCode, name, cuisine, priceRange, minRating, foodType) // Pass foodType here
            .stream()
            .filter(restaurant -> 
                (city == null || restaurant.getCity() != null && city.equalsIgnoreCase(restaurant.getCity())) &&
                (zipCode == null || restaurant.getZipCode() != null && zipCode.equals(restaurant.getZipCode()))
            )
            .map(this::convertToDto)
            .collect(Collectors.toList());

    // Fetch external restaurants
    List<RestaurantDto> externalRestaurants = new ArrayList<>();
    if (zipCode != null || city != null || country != null) {
        try {
            LocationDto location = geocodingService.getCoordinates(zipCode, city, country);
            externalRestaurants = fetchExternalRestaurants(location);
        } catch (Exception e) {
            System.err.println("Error fetching external restaurants: " + e.getMessage());
        }
    }

    // Combine results into wrapper class
    RestaurantSearchResponse response = new RestaurantSearchResponse();
    response.setInternalRestaurants(internalRestaurants);
    response.setExternalRestaurants(externalRestaurants);

    return response;
}

}

