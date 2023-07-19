package com.example.apm.repository;

import com.example.apm.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Integer> {
    Seat findBySeatName(String seatName);
    List<Seat> findAll();
}
