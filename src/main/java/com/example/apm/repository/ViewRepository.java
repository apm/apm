package com.example.apm.repository;

import com.example.apm.entity.View;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.apm.entity.Seat;

public interface ViewRepository extends JpaRepository<View, Integer> {
    View findBySeat(Seat seat);
}
