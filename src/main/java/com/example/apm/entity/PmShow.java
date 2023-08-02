package com.example.apm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PmShow { //show가 예약어여서 pmShow로 설정
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pmShowId;

    @Column(nullable = false)
    private String title; //공연제목

    @Column(nullable = false)
    private String ShowDate; //공연기간

    @Column(nullable = false)
    private String genre; //장르

    private String runningTime; //러닝타임

    @Column(nullable = false)
    private String synopsis; //시놉시스

    private String poster; //포스터 이미지 링크

    @ManyToOne
    @JoinColumn(name = "theater_id") // 외래키
    private Theater theater;
}
