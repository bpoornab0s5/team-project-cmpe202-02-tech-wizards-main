package com.example.restaurantfinder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restaurantfinder.dto.ReviewDto;
import com.example.restaurantfinder.dto.ReviewSummaryDto;
import com.example.restaurantfinder.model.Review;
import com.example.restaurantfinder.service.ReviewService;

@RestController
@RequestMapping("/api/restaurants")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;


    // Fetch all reviews for a specific restaurant
    @GetMapping("/{restaurantId}/reviews")
    public ResponseEntity<List<ReviewDto>> getReviewsForRestaurant(@PathVariable Long restaurantId) {
        List<ReviewDto> reviews = reviewService.getReviewsForRestaurant(restaurantId);
        return ResponseEntity.ok(reviews);
    }
    

    // Submit a new review for a restaurant
    @PostMapping("/{restaurantId}/reviews")
    public ResponseEntity<ReviewDto> submitReview(
            @PathVariable Long restaurantId, @RequestBody ReviewDto reviewDto) {
        ReviewDto createdReview = reviewService.submitReview(restaurantId, reviewDto);
        return ResponseEntity.ok(createdReview);
    }
    
    @PutMapping("/{restaurantId}/reviews/{reviewId}")
    public ResponseEntity<ReviewDto> updateReview(
            @PathVariable Long restaurantId,
            @PathVariable Long reviewId,
            @RequestBody ReviewDto reviewDto) {
    
        Review updatedReview = reviewService.updateReview(restaurantId, reviewId, reviewDto);
    
        ReviewDto responseDto = new ReviewDto(
                updatedReview.getId(),
                updatedReview.getRating(),
                updatedReview.getComment(),
                updatedReview.getRestaurant().getId(),
                updatedReview.getUser().getId()
        );
    
        return ResponseEntity.ok(responseDto);
    }
    
    
    // Delete a review
    @DeleteMapping("/{restaurantId}/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long restaurantId,
            @PathVariable Long reviewId) {
        reviewService.deleteReview(restaurantId, reviewId);
        return ResponseEntity.noContent().build();
    }

    // Get a summary of reviews for a specific restaurant
    @GetMapping("/{restaurantId}/reviews-summary")
    public ResponseEntity<ReviewSummaryDto> getReviewSummary(@PathVariable Long restaurantId) {
        ReviewSummaryDto reviewSummary = reviewService.getReviewSummary(restaurantId);
        return ResponseEntity.ok(reviewSummary);
    }
}
