package com.example.spring_boot_jwt_ntn.repository;

import com.example.spring_boot_jwt_ntn.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
