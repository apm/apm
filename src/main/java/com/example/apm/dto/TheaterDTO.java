package com.example.apm.dto;

import com.example.apm.entity.Theater;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TheaterDTO {
    private Integer theaterId;
    private String theaterName;
    private String theaterLocation;
    private String theaterRecentPlay1;
    private String theaterRecentPlay2;
    private String theaterRecentPlay3;

    // 생성자
    public TheaterDTO(Integer theaterId, String theaterName, String theaterLocation,
                      String theaterRecentPlay1, String theaterRecentPlay2, String theaterRecentPlay3) {
        this.theaterId = theaterId;
        this.theaterName = theaterName;
        this.theaterLocation = theaterLocation;
        this.theaterRecentPlay1 = theaterRecentPlay1;
        this.theaterRecentPlay2 = theaterRecentPlay2;
        this.theaterRecentPlay3 = theaterRecentPlay3;
    }

    // 엔티티를 DTO로 변환하는 정적 메서드
    public static TheaterDTO from(Theater theater) {
        return new TheaterDTO(
                theater.getTheaterId(),
                theater.getTheaterName(),
                theater.getTheaterLocation(),
                theater.getTheaterRecentPlay1(),
                theater.getTheaterRecentPlay2(),
                theater.getTheaterRecentPlay3()
        );
    }
}
