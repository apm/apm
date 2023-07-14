package com.example.apm.repository;

import com.example.apm.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, String> {
    Optional<SiteUser> findByusername(String username);
}
