package com.example.restaurantfinder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.restaurantfinder.model.Restaurant;


@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByOwnerId(Long ownerId);

    // Custom method to find restaurants by address containing a specific ZIP code
    List<Restaurant> findByAddressContaining(String zipcode);

    List<Restaurant> findByCuisineAndPriceRange(String cuisine, String priceRange);

    // Method for filtering based on multiple conditions
    List<Restaurant> findByNameContainingOrAddressContaining(String name, String address);

    // @Query("SELECT r FROM Restaurant r WHERE "
    //         + "(?1 IS NULL OR r.zipCode = ?1) AND "
    //         + "(?2 IS NULL OR r.name LIKE %?2%) AND "
    //         + "(?3 IS NULL OR r.cuisine = ?3) AND "
    //         + "(?4 IS NULL OR r.priceRange = ?4) AND "
    //         + "(?5 IS NULL OR r.rating >= ?5)")
    // List<Restaurant> findByFilters(String zipCode, String name, String cuisine, String priceRange, Double minRating);
    
    @Query("SELECT r FROM Restaurant r WHERE " +
    "(:zipCode IS NULL OR r.zipCode = :zipCode) AND " +
    "(:name IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
    "(:cuisine IS NULL OR r.cuisine = :cuisine) AND " +
    "(:priceRange IS NULL OR r.priceRange = :priceRange) AND " +
    "(:minRating IS NULL OR r.rating >= :minRating) AND " +
    "(:foodType IS NULL OR r.foodType = :foodType)")
List<Restaurant> findByFilters(
    @Param("zipCode") String zipCode,
    @Param("name") String name,
    @Param("cuisine") String cuisine,
    @Param("priceRange") String priceRange,
    @Param("minRating") Double minRating,
    @Param("foodType") String foodType
);


    @Query("SELECT r FROM Restaurant r WHERE "
       + "r.name IN (SELECT r2.name FROM Restaurant r2 GROUP BY r2.name, r2.address HAVING COUNT(r2) > 1) "
       + "AND r.address IN (SELECT r2.address FROM Restaurant r2 GROUP BY r2.name, r2.address HAVING COUNT(r2) > 1)")
    List<Restaurant> findDuplicateRestaurants();
    // Fetch restaurants reported as closed
    List<Restaurant> findByIsReportedClosedTrue();
    // Search for restaurants by zipcode
    // List<Restaurant> findByZipCode(String zipCode);

    // @Query("SELECT r FROM Restaurant r WHERE "
    //     + "(?1 IS NULL OR r.zipCode = ?1)")
    // Page<Restaurant> findByZipCode(String zipCode, Pageable pageable);

    @Query("SELECT r FROM Restaurant r WHERE r.zipCode = :zipCode")
    List<Restaurant> findByZipCode(@Param("zipCode") String zipCode);


}
