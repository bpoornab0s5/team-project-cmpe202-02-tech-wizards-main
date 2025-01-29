package com.example.restaurantfinder.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.restaurantfinder.dto.LocationDto;
import com.example.restaurantfinder.exception.GeocodingException;

@Service
public class GeocodingService {

    @Value("${google.maps.api.key}")
    private String apiKey;

    private static final String GEOCODING_URL = "https://maps.googleapis.com/maps/api/geocode/json";

    public LocationDto getCoordinates(String zipCode, String city, String country) {
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder query = new StringBuilder();
    
        if (zipCode != null) query.append(zipCode);
        if (city != null) query.append(",").append(city);
        if (country != null) query.append(",").append(country);
    
        String url = GEOCODING_URL + "?address=" + query.toString() + "&key=" + apiKey;
        System.out.println("Geocoding API URL: " + url);
    
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        System.out.println("Geocoding API Response: " + response);
    
        if (response == null || !"OK".equals(response.get("status"))) {
            throw new GeocodingException("Failed to fetch geocoding data.");
        }
    
        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
        if (results == null || results.isEmpty()) {
            throw new GeocodingException("No geocoding results found.");
        }
    
        Map<String, Object> location = (Map<String, Object>) ((Map<String, Object>) results.get(0).get("geometry")).get("location");
        return new LocationDto(
            (Double) location.get("lat"),
            (Double) location.get("lng")
        );
    }
    
    
}