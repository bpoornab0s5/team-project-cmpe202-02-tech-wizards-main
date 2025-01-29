package com.example.restaurantfinder.dto;

public class LocationDto {
    private double latitude;
    private double longitude;

    // Constructors
    public LocationDto() {}

    public LocationDto(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
