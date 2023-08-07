package com.example.apm.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Theater {
    @Id
    private Integer theaterId;

    @Column(nullable = false)
    private String theaterName; //극장명

    private String theaterLocation;

    private String theaterPhoneNumber;

    private String theaterHomePage;

    private Integer theaterTotalSeat;

    @OneToMany(mappedBy = "theater", cascade = CascadeType.REMOVE) //극장이 삭제되면 달린 좌석까지 삭제
    private List<Seat> seatList;

    public Theater(){

    }

    public Theater(Integer theaterId, String theaterName, String theaterLocation,
                   String theaterPhoneNumber, String theaterHomePage,
                   List<Seat> seatList, Integer theaterTotalSeat) {
        this.theaterId = theaterId;
        this.theaterName = theaterName;
        this.theaterLocation = theaterLocation;
        this.theaterPhoneNumber = theaterPhoneNumber;
        this.theaterHomePage = theaterHomePage;
        this.seatList = seatList;
        this.theaterTotalSeat = theaterTotalSeat;
    }
}
