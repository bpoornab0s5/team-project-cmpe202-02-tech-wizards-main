// src/main/java/com/example/restaurantfinder/config/DataInitializer.java

package com.example.restaurantfinder.config;

import com.example.restaurantfinder.model.Option;
import com.example.restaurantfinder.repository.OptionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner loadData(OptionRepository optionRepository) {
        return args -> {
            if (optionRepository.count() == 0) {  // Check if options table is empty

                List<Option> defaultOptions = Arrays.asList(
                        new Option("category", "Greek"),
                        new Option("category", "Mexican"),
                        new Option("category", "Italian"),
                        new Option("category", "Chinese"),
                        new Option("foodType", "Vegetarian"),
                        new Option("foodType", "Vegan"),
                        new Option("foodType", "Gluten-Free"),
                        new Option("foodType", "Non-Vegetarian"),
                        new Option("priceRange", "$"),
                        new Option("priceRange", "$$"),
                        new Option("priceRange", "$$$")
                );

                optionRepository.saveAll(defaultOptions);
            }
        };
    }
}
