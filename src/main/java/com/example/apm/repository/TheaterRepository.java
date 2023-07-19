package com.example.apm.repository;

import com.example.apm.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepository extends JpaRepository<Theater, Integer> {
    Theater findByTheaterName(String theaterName);
}
