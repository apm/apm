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
    private String pmShowId;

    @Column(nullable = false)
    private String title; //공연제목

    private String theaterName;

    private String showDate; //공연기간

    private String runningTime; //러닝타임

    private String viewingAge;

    private String genre; //장르

    private String otherNotice;

    private String castingActor;

    private String castingName;

    private String castingInformation;

    private String notice1;

    private String notice2;

    private String saleinfo1;

    private String saleinfo2;

    private String playinfo1;

    private String playinfo2;

    private String castinginfo1;

    private String castinginfo2;

    private String synopsis; //시놉시스


    private String poster; //포스터 이미지 링크

    @ManyToOne
    @JoinColumn(name = "theater_id") // 외래키
    private Theater theater;

    public PmShow() {
    }

    public PmShow(String pmShowId, String title, String theaterName, String showDate,
                  String runningTime, String viewingAge, String otherNotice,
                  String castingActor, String castingName, String castingInformation,
                  String notice1, String notice2, String saleinfo1,
                  String saleinfo2, String playinfo1, String playinfo2,
                  String castinginfo1, String castinginfo2, String genre,
                  String synopsis, String poster) {
        this.pmShowId = pmShowId;
        this.title = title;
        this.theaterName = theaterName;
        this.showDate = showDate;
        this.runningTime = runningTime;
        this.viewingAge = viewingAge;
        this.otherNotice = otherNotice;
        this.castingActor = castingActor;
        this.castingName = castingName;
        this.castingInformation = castingInformation;
        this.notice1 = notice1;
        this.notice2 = notice2;
        this.saleinfo1 = saleinfo1;
        this.saleinfo2 = saleinfo2;
        this.playinfo1 = playinfo1;
        this.playinfo2 = playinfo2;
        this.castinginfo1 = castinginfo1;
        this.castinginfo2 = castinginfo2;
        this.genre = genre;
        this.synopsis = synopsis;
        this.poster = poster;

    }
}
