// src/main/java/com/example/restaurantfinder/service/OptionService.java

package com.example.restaurantfinder.service;

import com.example.restaurantfinder.model.Option;
import com.example.restaurantfinder.repository.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionService {

    @Autowired
    private OptionRepository optionRepository;

    public List<Option> getCategories() {
        return optionRepository.findByType("category");
    }

    public List<Option> getFoodTypes() {
        return optionRepository.findByType("foodType");
    }

    public List<Option> getPriceRanges() {
        return optionRepository.findByType("priceRange");
    }
}
