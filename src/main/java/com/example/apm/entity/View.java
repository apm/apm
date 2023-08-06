package com.example.apm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class View {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer viewId;

    @Column(columnDefinition = "TEXT") //글자 수 제한x
    private String comment; //시야정보

    @Column(nullable = false)
    @Min(value = 1)
    @Max(value = 5)
    private int seatScore; //좌석점수

    @ManyToOne //FK
    private Seat seat; //시야정보에서 좌석엔티티를 참조하도록 설정. 시야 객체를 통해 좌석명을 알고 싶다면 view.getSeat().getSeatName();

    private LocalDateTime writeDate;

    @ManyToOne
    private SiteUser writer; //뷰 작성자

    @Builder
    public View(Seat seat, String comment, int seatScore, SiteUser writer){
        this.comment = comment;
        this.seatScore = seatScore;
        this.seat = seat;
        this.writer = writer; //뷰 저장시 작성자 저장됨
    }
}