package com.example.restaurantfinder.dto;

public class ReviewSummaryDto {
    private double averageRating;
    private int totalReviews;

    // Constructors
    public ReviewSummaryDto() {}

    public ReviewSummaryDto(double averageRating, int totalReviews) {
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
    }

    // Getters and Setters
    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(int totalReviews) {
        this.totalReviews = totalReviews;
    }
}
