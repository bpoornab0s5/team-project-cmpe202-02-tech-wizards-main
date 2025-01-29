// src/main/java/com/example/restaurantfinder/repository/OptionRepository.java

package com.example.restaurantfinder.repository;

import com.example.restaurantfinder.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findByType(String type);
}
