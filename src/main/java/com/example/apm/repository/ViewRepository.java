package com.example.apm.repository;

import com.example.apm.entity.View;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.apm.entity.Seat;

import java.util.List;
import java.util.Optional;

public interface ViewRepository extends JpaRepository<View, Integer> {
    View findBySeat(Seat seat);

    Optional<View> findByViewId(Integer ViewId);

    List<View> findBySeatScore(int seatScore);
}
