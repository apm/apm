package com.example.apm.service;

import com.example.apm.entity.Seat;
import com.example.apm.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SeatService {
    private final SeatRepository seatRepository;

    public List<Seat> getList(){
        return this.seatRepository.findAll(); //좌석목록을 조회해서 리턴
    }
}
