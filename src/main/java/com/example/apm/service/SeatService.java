package com.example.apm.service;

import com.example.apm.DataNotFoundException;
import com.example.apm.entity.Seat;
import com.example.apm.entity.View;
import com.example.apm.repository.SeatRepository;
import com.example.apm.repository.ViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SeatService {
    private final SeatRepository seatRepository;
    public List<Seat> getSeatList(){
        return this.seatRepository.findAll(); //좌석목록을 조회해서 리턴
    }

    public Seat getSeat(Integer seatId){
        Optional<Seat> seat = this.seatRepository.findBySeatId(seatId);
        if(seat.isPresent()){
            return seat.get();
        } else {
            throw new DataNotFoundException("좌석 정보가 없습니다.");
        }
    } //특정 좌석 조회

    public List<Seat> getSeatsByTheaterId(Integer theaterId) {
        return seatRepository.findByTheaterTheaterId(theaterId);
    } //극장아이디 받아와서 해당 극장좌석 조회에 참고



}
