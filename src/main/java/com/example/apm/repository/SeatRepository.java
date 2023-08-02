package com.example.apm.repository;

import com.example.apm.entity.Seat;
import com.example.apm.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Integer> {
    Seat findBySeatName(String seatName);
    List<Seat> findAll();

    Optional<Seat> findBySeatId(Integer seatId);

    List<Seat> findByTheaterTheaterId(Integer theaterId);
}
