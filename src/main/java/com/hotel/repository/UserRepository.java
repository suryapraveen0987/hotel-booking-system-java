package com.hotel.repository;

import com.hotel.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// JpaRepository<Entity Class, Primary Key Type>
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a User by their unique username.
     * Spring Data JPA automatically generates the query (SELECT * FROM users WHERE username = ?)
     * based on the method name format (findBy + FieldName).
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a user with the given username already exists.
     * Useful for registration validation.
     */
    boolean existsByUsername(String username);
}