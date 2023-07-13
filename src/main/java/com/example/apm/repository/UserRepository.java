package com.example.apm.repository;

import com.example.apm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByUserId(String userId);
}
