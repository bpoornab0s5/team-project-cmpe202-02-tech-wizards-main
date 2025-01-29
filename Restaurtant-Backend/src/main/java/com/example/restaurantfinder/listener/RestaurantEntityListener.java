// package com.example.restaurantfinder.listener;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;

// import com.example.restaurantfinder.model.Restaurant;
// import com.example.restaurantfinder.model.Review;
// import com.example.restaurantfinder.repository.ReviewRepository;

// import jakarta.persistence.PostLoad;
// import jakarta.persistence.PostPersist;
// import jakarta.persistence.PostUpdate;

// public class RestaurantEntityListener {

//     @Autowired
//     private ReviewRepository reviewRepository;

//     @PostLoad
//     @PostPersist
//     @PostUpdate
//     public void updateAverageRating(Restaurant restaurant) {
//         // Fetch all reviews for the restaurant
//         List<Review> reviews = reviewRepository.findByRestaurantId(restaurant.getId());

//         // Calculate average rating
//         double averageRating = reviews.stream()
//                 .mapToDouble(Review::getRating)
//                 .average()
//                 .orElse(0.0);

//         // Update the restaurant's rating
//         restaurant.setRating(averageRating);
//     }
// }
