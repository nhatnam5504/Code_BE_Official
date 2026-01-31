package com.example.code_be.repository;

import com.example.code_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPin(String pin);

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndPin(String username, String pin);
}
