package com.example.apm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.apm.DataNotFoundException;
import com.example.apm.entity.Theater;
import com.example.apm.repository.TheaterRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TheaterService {
    private final TheaterRepository theaterRepository;
    private final SeatService seatService;

//    public List<Theater> getTheaterList() {
//        return this.theaterRepository.findAll();
//    }

    public Theater getTheater(Integer theaterId) {
        Optional<Theater> theater = this.theaterRepository.findByTheaterId(theaterId);
        if (theater.isPresent()) {
            return theater.get();
        } else {
            throw new DataNotFoundException("극장 정보가 없습니다.");
        }
    }

    public Page<Theater> getTheaterList(int page){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("theaterName"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts)); //이름순으로 정렬
        return this.theaterRepository.findAll(pageable);
    }
}
