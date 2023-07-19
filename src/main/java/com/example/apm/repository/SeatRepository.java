package com.example.apm.repository;

import com.example.apm.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Integer> {
    Seat findBySeatName(String seatName);
}
