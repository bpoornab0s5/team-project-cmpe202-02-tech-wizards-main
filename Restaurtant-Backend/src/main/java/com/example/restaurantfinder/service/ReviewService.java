// package com.example.restaurantfinder.service;

// import java.util.List;
// import java.util.stream.Collectors;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.example.restaurantfinder.dto.ReviewDto;
// import com.example.restaurantfinder.dto.ReviewSummaryDto;
// import com.example.restaurantfinder.exception.ResourceNotFoundException;
// import com.example.restaurantfinder.model.Restaurant;
// import com.example.restaurantfinder.model.Review;
// import com.example.restaurantfinder.model.User;
// import com.example.restaurantfinder.repository.RestaurantRepository;
// import com.example.restaurantfinder.repository.ReviewRepository;
// import com.example.restaurantfinder.repository.UserRepository;
// @Service
// public class ReviewService {

//     @Autowired
//     private ReviewRepository reviewRepository;

//     @Autowired
//     private RestaurantRepository restaurantRepository;

//     @Autowired
//     private UserRepository userRepository;


//     public List<ReviewDto> getReviewsForRestaurant(Long restaurantId) {
//         // Ensure the restaurant exists
//         Restaurant restaurant = restaurantRepository.findById(restaurantId)
//                 .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id " + restaurantId));
    
//         // Fetch the reviews for the restaurant
//         List<Review> reviews = reviewRepository.findByRestaurantId(restaurant.getId());
    
//         // Convert each Review entity to ReviewDto
//         return reviews.stream()
//                 .map(review -> new ReviewDto(
//                         review.getId(),
//                         review.getRating(),
//                         review.getComment(),
//                         review.getRestaurant().getId(),
//                         review.getUser() != null ? review.getUser().getId() : null // Handle null User
//                 ))
//                 .toList();
//     }
    

//     public Review submitReview(Long restaurantId, Review review) {
//         Restaurant restaurant = restaurantRepository.findById(restaurantId)
//                 .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id " + restaurantId));
//         review.setRestaurant(restaurant);
//         return reviewRepository.save(review);
//     }

//     public void deleteReview(Long reviewId) {
//         Review review = reviewRepository.findById(reviewId)
//                 .orElseThrow(() -> new ResourceNotFoundException("Review not found with id " + reviewId));
//         reviewRepository.delete(review);
//     }

//     public Review updateReview(Long restaurantId, Long reviewId, ReviewDto reviewDto) {
//         // Ensure the restaurant exists
//         Restaurant restaurant = restaurantRepository.findById(restaurantId)
//                 .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id " + restaurantId));
    
//         // Ensure the review exists
//         Review review = reviewRepository.findById(reviewId)
//                 .orElseThrow(() -> new ResourceNotFoundException("Review not found with id " + reviewId));
    
//         // Ensure the review belongs to the correct restaurant
//         if (!review.getRestaurant().getId().equals(restaurantId)) {
//             throw new RuntimeException("Review does not belong to the specified restaurant");
//         }
    
//         // Update the review fields
//         review.setRating(reviewDto.getRating());
//         review.setComment(reviewDto.getComment());
    
//         // Save and return the updated review
//         return reviewRepository.save(review);
//     }
    

//     // Method to get the summary of reviews for a specific restaurant
//     public ReviewSummaryDto getReviewSummary(Long restaurantId) {
//         Restaurant restaurant = restaurantRepository.findById(restaurantId)
//                 .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id " + restaurantId));

//         List<Review> reviews = reviewRepository.findByRestaurantId(restaurantId);

//         double averageRating = reviews.stream()
//                 .mapToDouble(Review::getRating)
//                 .average()
//                 .orElse(0.0);

//         int totalReviews = reviews.size();

//         return new ReviewSummaryDto(averageRating, totalReviews);
//     }


//     // Delete a review
//     public void deleteReview(Long restaurantId, Long reviewId) {
//         Restaurant restaurant = restaurantRepository.findById(restaurantId)
//                 .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id " + restaurantId));

//         Review review = reviewRepository.findById(reviewId)
//                 .orElseThrow(() -> new ResourceNotFoundException("Review not found with id " + reviewId));

//         if (!review.getRestaurant().getId().equals(restaurant.getId())) {
//             throw new RuntimeException("Review does not belong to the specified restaurant");
//         }

//         reviewRepository.delete(review);
//     }


//     public ReviewDto submitReview(Long restaurantId, ReviewDto reviewDto) {
//         // Ensure the restaurant exists
//         Restaurant restaurant = restaurantRepository.findById(restaurantId)
//                 .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id " + restaurantId));
    
//         // Ensure the user exists
//         User user = userRepository.findById(reviewDto.getUserId())
//                 .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + reviewDto.getUserId()));
    
//         // Convert DTO to Review entity
//         Review review = new Review();
//         review.setRating(reviewDto.getRating());
//         review.setComment(reviewDto.getComment());
//         review.setRestaurant(restaurant);
//         review.setUser(user); // Attach the managed User entity
    
//         // Save the review
//         Review savedReview = reviewRepository.save(review);
    
//         // Convert the saved review back to DTO
//         return new ReviewDto(
//                 savedReview.getId(),
//                 savedReview.getRating(),
//                 savedReview.getComment(),
//                 savedReview.getRestaurant().getId(),
//                 savedReview.getUser().getId()
//         );
//     }
    
//         public List<ReviewDto> getReviewsByUser(Long userId) {
//         // Ensure the user exists
//         User user = userRepository.findById(userId)
//                 .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

//         // Fetch reviews for the user
//         List<Review> reviews = reviewRepository.findByUserId(userId);

//         // Convert reviews to DTOs
//         return reviews.stream()
//                 .map(review -> new ReviewDto(
//                         review.getId(),
//                         review.getRating(),
//                         review.getComment(),
//                         review.getRestaurant().getId(),
//                         review.getUser().getId()
//                 ))
//                 .collect(Collectors.toList());
//     }
    
    
// }

package com.example.restaurantfinder.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restaurantfinder.dto.ReviewDto;
import com.example.restaurantfinder.dto.ReviewSummaryDto;
import com.example.restaurantfinder.exception.ResourceNotFoundException;
import com.example.restaurantfinder.model.Restaurant;
import com.example.restaurantfinder.model.Review;
import com.example.restaurantfinder.model.User;
import com.example.restaurantfinder.repository.RestaurantRepository;
import com.example.restaurantfinder.repository.ReviewRepository;
import com.example.restaurantfinder.repository.UserRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    // Helper method to update the restaurant's average rating
    private void updateRestaurantRating(Long restaurantId) {
        List<Review> reviews = reviewRepository.findByRestaurantId(restaurantId);

        double averageRating = reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id " + restaurantId));

        restaurant.setRating(averageRating);
        restaurantRepository.save(restaurant);
    }

    public List<ReviewDto> getReviewsForRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id " + restaurantId));

        List<Review> reviews = reviewRepository.findByRestaurantId(restaurant.getId());

        return reviews.stream()
                .map(review -> new ReviewDto(
                        review.getId(),
                        review.getRating(),
                        review.getComment(),
                        review.getRestaurant().getId(),
                        review.getUser() != null ? review.getUser().getId() : null
                ))
                .toList();
    }

    public Review submitReview(Long restaurantId, Review review) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id " + restaurantId));

        review.setRestaurant(restaurant);
        Review savedReview = reviewRepository.save(review);

        updateRestaurantRating(restaurantId); // Update restaurant rating
        return savedReview;
    }

    public Review updateReview(Long restaurantId, Long reviewId, ReviewDto reviewDto) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id " + restaurantId));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id " + reviewId));

        if (!review.getRestaurant().getId().equals(restaurantId)) {
            throw new RuntimeException("Review does not belong to the specified restaurant");
        }

        review.setRating(reviewDto.getRating());
        review.setComment(reviewDto.getComment());
        Review updatedReview = reviewRepository.save(review);

        updateRestaurantRating(restaurantId); // Update restaurant rating
        return updatedReview;
    }

    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id " + reviewId));

        Long restaurantId = review.getRestaurant().getId();
        reviewRepository.delete(review);

        updateRestaurantRating(restaurantId); // Update restaurant rating
    }

    public void deleteReview(Long restaurantId, Long reviewId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id " + restaurantId));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id " + reviewId));

        if (!review.getRestaurant().getId().equals(restaurant.getId())) {
            throw new RuntimeException("Review does not belong to the specified restaurant");
        }

        reviewRepository.delete(review);
        updateRestaurantRating(restaurantId); // Update restaurant rating
    }

    public ReviewDto submitReview(Long restaurantId, ReviewDto reviewDto) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id " + restaurantId));

        User user = userRepository.findById(reviewDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + reviewDto.getUserId()));

        Review review = new Review();
        review.setRating(reviewDto.getRating());
        review.setComment(reviewDto.getComment());
        review.setRestaurant(restaurant);
        review.setUser(user);

        Review savedReview = reviewRepository.save(review);

        updateRestaurantRating(restaurantId); // Update restaurant rating
        return new ReviewDto(
                savedReview.getId(),
                savedReview.getRating(),
                savedReview.getComment(),
                savedReview.getRestaurant().getId(),
                savedReview.getUser().getId()
        );
    }

    public ReviewSummaryDto getReviewSummary(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id " + restaurantId));

        List<Review> reviews = reviewRepository.findByRestaurantId(restaurantId);

        double averageRating = reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);

        int totalReviews = reviews.size();

        return new ReviewSummaryDto(averageRating, totalReviews);
    }

    public List<ReviewDto> getReviewsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        List<Review> reviews = reviewRepository.findByUserId(userId);

        return reviews.stream()
                .map(review -> new ReviewDto(
                        review.getId(),
                        review.getRating(),
                        review.getComment(),
                        review.getRestaurant().getId(),
                        review.getUser().getId()
                ))
                .collect(Collectors.toList());
    }
}
