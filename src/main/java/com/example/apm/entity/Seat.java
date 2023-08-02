package com.example.apm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seatId;

    @Column(nullable = false)
    private String seatName; //좌석명

    @OneToMany(mappedBy = "seat", fetch = FetchType.EAGER)
    private List<View> viewList; //view 엔티티 객체로 구성된 viewList를 속성으로 추가. 좌석에서 시야정보 참조시 seat.getViewList() 호출

    @ManyToOne
    private Theater theater; //FK

    private int averageSeatScore; //좌석 평균 점수
}
