package com.example.apm.dto;

import com.example.apm.entity.Theater;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TheaterDTO {
    private Integer theaterId;
    private String theaterName;
    private String theaterLocation;
    private String theaterPhoneNumber;
    private String theaterHomePage;
    private Integer theaterTotalSeat;

    // 생성자
    public TheaterDTO(Integer theaterId, String theaterName, String theaterLocation, String theaterPhoneNumber, String theaterHomePage, Integer theaterTotalSeat) {
        this.theaterId = theaterId;
        this.theaterName = theaterName;
        this.theaterLocation = theaterLocation;
        this.theaterPhoneNumber = theaterPhoneNumber;
        this.theaterHomePage = theaterHomePage;
        this.theaterTotalSeat = theaterTotalSeat;
    }

    // 엔티티를 DTO로 변환하는 정적 메서드
    public static TheaterDTO from(Theater theater) {
        return new TheaterDTO(
                theater.getTheaterId(),
                theater.getTheaterName(),
                theater.getTheaterLocation(),
                theater.getTheaterPhoneNumber(),
                theater.getTheaterHomePage(),
                theater.getTheaterTotalSeat()
        );
    }
}
