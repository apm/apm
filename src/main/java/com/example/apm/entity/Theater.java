package com.example.apm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer theaterId;

    @Column(nullable = false)
    private String theaterName; //극장명

    private String theaterArea; //극장 주소

    private String theaterSeats; //좌석수

    @OneToMany(mappedBy = "theater", cascade = CascadeType.REMOVE) //극장이 삭제되면 달린 좌석까지 삭제
    private List<Seat> seatList;
}
