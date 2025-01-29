package com.example.restaurantfinder.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "restaurants")
// @EntityListeners(RestaurantEntityListener.class)
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Restaurant name is required")
    @Size(max = 100, message = "Restaurant name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "ContactInfo is Required")
    private String contactInfo;

    @NotBlank(message = "Operating Hours is required")
    private String hours;

    @NotBlank(message = "Cuisine type is required")
    private String cuisine;

    @NotBlank(message = "Food type is required")
    private String foodType;

    
    @NotBlank(message = "Price Range is required")
    private String priceRange;

    private double rating;

    @NotBlank(message = "Description is required")
    private String description;


    @ElementCollection
    private List<String> photos; // Storing photo URLs

    @ManyToOne
    @JoinColumn(name = "owner_id")
    // @JsonBackReference
    @JsonIgnore
    private User owner;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    // @JsonManagedReference
    @JsonIgnore
    private Set<Review> reviews = new HashSet<>();

    @Embedded
    private Location location;
    private String city;
    private String zipCode; // New Field
    private String state;   // New Field
    private String country; // New Field
    private boolean isReportedClosed = false; // New field

    // Getter and Setter for the new field
    public boolean isReportedClosed() {
        return isReportedClosed;
    }

    public void setReportedClosed(boolean isReportedClosed) {
        this.isReportedClosed = isReportedClosed;
    }


    // Getters and Setters
    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getHours() {
        return hours;
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

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    // existing getters and setters

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    // Getters and Setters
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

    public double getRating() {  // Getter for rating
        return rating;
    }

    public void setRating(double rating) {  // Setter for rating
        this.rating = rating;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
