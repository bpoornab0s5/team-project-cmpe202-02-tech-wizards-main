package com.example.restaurantfinder.dto;

import java.util.List;

public class RestaurantSearchResponse {
    private List<RestaurantDto> internalRestaurants;
    private List<RestaurantDto> externalRestaurants;

    public List<RestaurantDto> getInternalRestaurants() {
        return internalRestaurants;
    }

    public void setInternalRestaurants(List<RestaurantDto> internalRestaurants) {
        this.internalRestaurants = internalRestaurants;
    }

    public List<RestaurantDto> getExternalRestaurants() {
        return externalRestaurants;
    }

    public void setExternalRestaurants(List<RestaurantDto> externalRestaurants) {
        this.externalRestaurants = externalRestaurants;
    }
}
