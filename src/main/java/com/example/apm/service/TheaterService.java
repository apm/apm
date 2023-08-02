package com.example.apm.service;

import com.example.apm.DataNotFoundException;
import com.example.apm.entity.Seat;
import com.example.apm.entity.Theater;
import com.example.apm.entity.View;
import com.example.apm.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TheaterService {
    private final TheaterRepository theaterRepository;
    private final SeatService seatService;

    public List<Theater> getTheaterList() {
        return this.theaterRepository.findAll();
    }

    public Theater getTheater(Integer theaterId) {
        Optional<Theater> theater = this.theaterRepository.findByTheaterId(theaterId);
        if (theater.isPresent()) {
            return theater.get();
        } else {
            throw new DataNotFoundException("극장 정보가 없습니다.");
        }
    }
}
