package com.example.CampusJobBoard.repositories;

import com.example.CampusJobBoard.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/**
 * UserRepository provides database access methods for the User entity.
 * Extends JpaRepository to inherit common CRUD operations such as
 * save(), findById(), findAll(), and delete().
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /** Finds a user by their email address (used for login and validation). */
    Optional<User> findByEmail(String email);

    /** Checks if a user with the given email already exists. */
    boolean existsByEmail(String email);

    /** Counts how many users have a specific role (used to enforce ADMIN limit). */
    long countByRole(User.Role role);

    /** Returns all users that match a given role. */
    List<User> findByRole(User.Role role);

    /**
     * Checks whether a specific userId exists AND matches a given role.
     * Useful for validating before deletion or updates.
     */
    boolean existsByUserIdAndRole(Long userId, User.Role role);
}
