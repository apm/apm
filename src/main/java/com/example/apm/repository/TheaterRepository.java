package com.example.apm.repository;

import com.example.apm.entity.Theater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TheaterRepository extends JpaRepository<Theater, Integer> {
    Theater findByTheaterName(String theaterName);
    Optional<Theater> findByTheaterId(Integer theaterId);
    Page<Theater> findAll(Pageable pageable);
}
