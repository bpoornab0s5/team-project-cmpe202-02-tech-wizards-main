package com.example.restaurantfinder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restaurantfinder.model.User;
import com.example.restaurantfinder.model.User.Role;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find a user by their username
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    // Find a user by their username and role (useful for specific role-based authentication)
    Optional<User> findByUsernameAndRole(String username, Role role);

    // Find all users with a specific role (e.g., to retrieve all BusinessOwners or Admins)
    List<User> findByRole(Role role);

    // Check if a user with a specific email already exists (useful for registration validation)
    boolean existsByEmail(String email);

    // Check if a user with a specific username already exists
    boolean existsByUsername(String username);
}
