package com.repository;

import com.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByEmail(@NotBlank(message = "Email can not be blank") @Email(message = "Email is of incorrect format") String email);
}
