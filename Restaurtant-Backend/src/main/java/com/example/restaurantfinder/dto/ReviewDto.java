package com.example.restaurantfinder.dto;

import jakarta.validation.constraints.Size;

public class ReviewDto {
    private Long id;
    private int rating;

    // @NotBlank(message = "Email is required")
    @Size(max = 500, message = "Comment must not exceed 500 characters")
    private String comment;
    private Long restaurantId;
    private Long userId;

    // Getters and Setters

    public ReviewDto(Long id, int rating, String comment, Long restaurantId, Long userId) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.restaurantId = restaurantId;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
