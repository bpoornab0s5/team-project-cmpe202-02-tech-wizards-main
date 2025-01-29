package com.example.restaurantfinder.exception;

public class GeocodingException extends RuntimeException {
    public GeocodingException(String message) {
        super(message);
    }
}
