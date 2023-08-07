package com.example.apm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.apm.DataNotFoundException;
import com.example.apm.entity.Theater;
import com.example.apm.repository.TheaterRepository;

import lombok.RequiredArgsConstructor;

@Service
public class TheaterService {

    private final TheaterRepository theaterRepository;

    public TheaterService(TheaterRepository theaterRepository) {
        this.theaterRepository = theaterRepository;
    }

    public Theater getTheater(Integer theaterId) {
        return theaterRepository.findById(theaterId).orElse(null);
    }

    public List<Theater> getTheaterList() {
        return theaterRepository.findAll();
    }
}
