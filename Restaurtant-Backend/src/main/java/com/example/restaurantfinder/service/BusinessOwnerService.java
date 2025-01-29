
package com.example.restaurantfinder.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.example.restaurantfinder.dto.LocationDto;
import com.example.restaurantfinder.dto.RestaurantDto;
import com.example.restaurantfinder.exception.ResourceNotFoundException;
import com.example.restaurantfinder.model.Location;
import com.example.restaurantfinder.model.Restaurant;
import com.example.restaurantfinder.model.User;
import com.example.restaurantfinder.repository.RestaurantRepository;
import com.example.restaurantfinder.repository.UserRepository;

@Service
public class BusinessOwnerService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Add a new restaurant with the business owner assigned.
     *
     * @param restaurantDto DTO containing restaurant details.
     * @return The created restaurant as a DTO.
     */
    public RestaurantDto addRestaurant(RestaurantDto restaurantDto) {
        // Fetch the business owner using the provided ID
        User owner = userRepository.findById(restaurantDto.getBusinessOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Business owner not found with ID: " + restaurantDto.getBusinessOwnerId()));

        // Convert DTO to Entity and assign the owner
        Restaurant restaurant = convertToEntity(restaurantDto);
        restaurant.setOwner(owner);

        // Save the restaurant and return as DTO
        restaurant = restaurantRepository.save(restaurant);
        return convertToDto(restaurant);
    }

    /**
     * Update an existing restaurant.
     *
     * @param id            ID of the restaurant to update.
     * @param restaurantDto DTO containing updated restaurant details.
     * @return The updated restaurant as a DTO.
     */
    // public RestaurantDto updateRestaurant(Long id, RestaurantDto restaurantDto) {
    //     Restaurant restaurant = restaurantRepository.findById(id)
    //             .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with ID: " + id));

    //     // Update fields
    //     restaurant.setName(restaurantDto.getName());
    //     restaurant.setAddress(restaurantDto.getAddress());
    //     restaurant.setCuisine(restaurantDto.getCuisine());
    //     restaurant.setPriceRange(restaurantDto.getPriceRange());
    //     restaurant.setRating(restaurantDto.getRating());
    //     restaurant.setDescription(restaurantDto.getDescription());
    //     restaurant.setPhotos(restaurantDto.getPhotos());
    //     restaurant.setContactInfo(restaurantDto.getContactInfo());
    //     restaurant.setHours(restaurantDto.getHours());

    //     // Update location if provided
    //     if (restaurantDto.getLocation() != null) {
    //         Location location = new Location();
    //         location.setLatitude(restaurantDto.getLocation().getLatitude());
    //         location.setLongitude(restaurantDto.getLocation().getLongitude());
    //         restaurant.setLocation(location);
    //     }

    //     // Save and return updated DTO
    //     return convertToDto(restaurantRepository.save(restaurant));
    // }

    public RestaurantDto updateRestaurant(Long id, RestaurantDto restaurantDto) {
        // Fetch the existing restaurant by ID
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with ID: " + id));
    
        // Update restaurant fields from the DTO
        restaurant.setName(restaurantDto.getName());
        restaurant.setAddress(restaurantDto.getAddress());
        restaurant.setCuisine(restaurantDto.getCuisine());
        restaurant.setPriceRange(restaurantDto.getPriceRange());
        restaurant.setRating(restaurantDto.getRating());
        restaurant.setDescription(restaurantDto.getDescription());
        restaurant.setPhotos(restaurantDto.getPhotos());
        restaurant.setContactInfo(restaurantDto.getContactInfo());
        restaurant.setHours(restaurantDto.getHours());
        restaurant.setFoodType(restaurantDto.getFoodType());
        restaurant.setCity(restaurantDto.getCity());
        restaurant.setZipCode(restaurantDto.getZipCode());
        restaurant.setState(restaurantDto.getState());
        restaurant.setCountry(restaurantDto.getCountry());
        restaurant.setReportedClosed(restaurantDto.isReportedClosed());
    
        // Update location if provided
        if (restaurantDto.getLocation() != null) {
            Location location = restaurant.getLocation();
            if (location == null) {
                location = new Location();
            }
            location.setLatitude(restaurantDto.getLocation().getLatitude());
            location.setLongitude(restaurantDto.getLocation().getLongitude());
            restaurant.setLocation(location);
        } else {
            restaurant.setLocation(null); // Clear the location if not provided
        }
    
        // Save the updated restaurant entity
        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
    
        // Convert the updated entity back to DTO and return
        return convertToDto(updatedRestaurant);
    }

    /**
     * Delete a restaurant by ID.
     *
     * @param id ID of the restaurant to delete.
     */
    public void deleteRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with ID: " + id));
        restaurantRepository.delete(restaurant);
    }

    /**
     * Get all restaurants owned by a specific business owner.
     *
     * @param ownerId ID of the business owner.
     * @return List of restaurants as DTOs.
     */
    public List<RestaurantDto> getRestaurantsByOwner(Long ownerId) {
        List<Restaurant> restaurants = restaurantRepository.findByOwnerId(ownerId);
        return restaurants.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * Convert a RestaurantDto to a Restaurant entity.
     *
     * @param dto The RestaurantDto.
     * @return The Restaurant entity.
     */

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
        restaurant.setCity(dto.getCity());
        restaurant.setZipCode(dto.getZipCode());
        restaurant.setState(dto.getState());
        restaurant.setCountry(dto.getCountry());
        restaurant.setReportedClosed(dto.isReportedClosed()); // Add this line
    
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
                restaurant.getZipCode(), // Include zipCode
                restaurant.getState(),   // Include state
                restaurant.getCountry(), // Include country
                restaurant.getOwner() != null ? restaurant.getOwner().getId() : null, false
        );
    }

    /**
     * Get the RestaurantRepository.
     *
     * @return The RestaurantRepository.
     */
    public RestaurantRepository getRestaurantRepository() {
        return restaurantRepository;
    }

    /**
     * Set the RestaurantRepository.
     *
     * @param restaurantRepository The RestaurantRepository.
     */
    public void setRestaurantRepository(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }



    @Value("${azure.storage.connection-string}")
    private String azureConnectionString;

    @Value("${azure.storage.container-name}")
    private String containerName;

    // Create a BlobClient instance for Azure
    private BlobContainerClient getBlobContainerClient() {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(azureConnectionString)
                .buildClient();
        return blobServiceClient.getBlobContainerClient(containerName);
    }

    private String uploadFileToAzure(MultipartFile file) {
        try {
            BlobContainerClient containerClient = getBlobContainerClient();
            if (!containerClient.exists()) {
                containerClient.create();
            }

            String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            BlobClient blobClient = containerClient.getBlobClient(fileName);

            blobClient.upload(file.getInputStream(), file.getSize(), true);
            return blobClient.getBlobUrl(); // Return the public URL of the uploaded file
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to Azure Blob Storage", e);
        }
    }

    public String uploadPhoto(Long restaurantId, MultipartFile file) {
    // Fetch restaurant by ID
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with ID: " + restaurantId));

    // Upload file to Azure Blob Storage
    String photoUrl = uploadFileToAzure(file);

    // Add photo URL to the restaurant's photo list
    List<String> photos = restaurant.getPhotos() != null ? restaurant.getPhotos() : new ArrayList<>();
    photos.add(photoUrl);
    restaurant.setPhotos(photos);

    // Save restaurant with updated photos
    restaurantRepository.save(restaurant);

    return photoUrl;
}

public String updatePhoto(Long restaurantId, MultipartFile file) {
    // Fetch restaurant by ID
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with ID: " + restaurantId));

    // Upload new photo to Azure Blob Storage
    String photoUrl = uploadFileToAzure(file);

    // Replace existing photos with the new one
    List<String> photos = new ArrayList<>();
    photos.add(photoUrl);
    restaurant.setPhotos(photos);

    // Save restaurant with updated photos
    restaurantRepository.save(restaurant);

    return photoUrl;
}


}
