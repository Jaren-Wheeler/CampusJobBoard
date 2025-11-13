package com.example.CampusJobBoard.repositories;

import com.example.CampusJobBoard.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * UserRepository provides database access methods for the User entity.
 * Extends JpaRepository to inherit common CRUD operations such as
 * save(), findById(), findAll(), and delete().
 * Custom queries:
 * - findByEmail() → retrieves a user by their email address
 * - existsByEmail() → checks if a user already exists by email
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /** Finds a user by their email address (used for login and validation) */
    Optional<User> findByEmail(String email);

    /** Checks if a user with the given email already exists */
    boolean existsByEmail(String email);
}
