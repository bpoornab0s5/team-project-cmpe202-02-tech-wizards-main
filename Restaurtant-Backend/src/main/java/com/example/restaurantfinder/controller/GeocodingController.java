// package com.example.restaurantfinder.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.example.restaurantfinder.dto.LocationDto;
// import com.example.restaurantfinder.service.GeocodingService;

// @RestController
// @RequestMapping("/api/geocode")
// public class GeocodingController {

//     private final GeocodingService geocodingService;

//     @Autowired
//     public GeocodingController(GeocodingService geocodingService) {
//         this.geocodingService = geocodingService;
//     }

//     @GetMapping("/{zipcode}")
//     public ResponseEntity<LocationDto> getCoordinatesByZipCode(@PathVariable String zipcode) {
//         LocationDto location = geocodingService.getCoordinates(zipcode);
//         return ResponseEntity.ok(location);
//     }
// }
