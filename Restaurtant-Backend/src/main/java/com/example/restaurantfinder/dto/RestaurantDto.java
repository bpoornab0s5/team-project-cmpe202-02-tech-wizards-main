package com.example.restaurantfinder.dto;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
// @AllArgsConstructor
public class RestaurantDto {
    private Long id;

    @NotNull(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name should be between 2 and 100 characters")
    private String name;
    @NotNull(message = "Address is required")
    private String address;
    @NotNull(message = "Address is required")
    private String contactInfo; // New field
    @NotNull(message = "Address is required")
    private String hours;       // New field
    @NotNull(message = "Address is required")
    private String cuisine;
    @NotNull(message = "Address is required")
    private String foodType;
    @NotNull(message = "Address is required")
    private String priceRange;
    @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be 0 or higher")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating must be 5 or lower")
    private double rating;
    private String description;  // New field
    private List<String> photos; // New field
    private LocationDto location;
    private String city;
    private String zipCode; // New Field
    private String state;   // New Field
    private String country; // New Field
    private Long businessOwnerId;
    @Column(name = "is_reported_closed", nullable = false, columnDefinition = "boolean default false")
    private boolean isReportedClosed = false;




    public RestaurantDto(Long id, String name, String address, String contactInfo, String hours, String cuisine, String foodType, String priceRange, double rating, String description, List<String> photos, LocationDto location, String city, String zipCode, String state, String country, Long businessOwnerId, boolean isReportedClosed) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contactInfo = contactInfo;
        this.hours = hours;
        this.cuisine = cuisine;
        this.foodType = foodType;
        this.priceRange = priceRange;
        this.rating = rating;
        this.description = description;
        this.photos = photos;
        this.location = location;
        this.city = city;
        this.zipCode = zipCode;
        this.state = state;
        this.country = country;
        this.businessOwnerId = businessOwnerId;
        this.isReportedClosed = isReportedClosed;
    }

    public boolean isReportedClosed() {
        return isReportedClosed;
    }

    public void setReportedClosed(boolean reportedClosed) {
        isReportedClosed = reportedClosed;
    }    

//    public RestaurantDto(Long id, String name, String address, String cuisine, String priceRange, double rating, String description, List<String> photos, String contactInfo, String hours, Long id1) {
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public LocationDto getLocation() {
        return location;
    }

    public void setLocation(LocationDto location) {
        this.location = location;
    }

    public Long getBusinessOwnerId() {
        return businessOwnerId;
    }

    public void setBusinessOwnerId(Long businessOwnerId) {
        this.businessOwnerId = businessOwnerId;
    }

    public String getContactInfo() {
        return contactInfo;
    }

//    public RestaurantDto(Long id, String name, String address, String contactInfo, String hours, String cuisine, String foodType, String priceRange, double rating, String description, List<String> photos, LocationDto location, String zipCode, String state, String country, Long businessOwnerId) {
//        this.id = id;
//        this.name = name;
//        this.address = address;
//        this.contactInfo = contactInfo;
//        this.hours = hours;
//        this.cuisine = cuisine;
//        this.foodType = foodType;
//        this.priceRange = priceRange;
//        this.rating = rating;
//        this.description = description;
//        this.photos = photos;
//        this.location = location;
//        this.zipCode = zipCode;
//        this.state = state;
//        this.country = country;
//        this.businessOwnerId = businessOwnerId;
//    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }
    
    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    // public RestaurantDto(Long id2, String name2, String address2, String contactInfo2, String hours2, String cuisine2,
    //         String foodType2, String priceRange2, double rating2, String description2, List<String> photos2,
    //         Object object, String city2, String zipCode2, String state2, String country2, Long long1) {
    //     //TODO Auto-generated constructor stub
    // }
}

    
