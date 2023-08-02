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

    private String theaterRecentPlay1;

    private String theaterRecentPlay2;

    private String theaterRecentPlay3;



    @OneToMany(mappedBy = "theater", cascade = CascadeType.REMOVE) //극장이 삭제되면 달린 좌석까지 삭제
    private List<Seat> seatList;

    public Theater(){

    }

    public Theater(Integer theaterId, String theaterName, String theaterLocation,
                   String theaterRecentPlay1, String theaterRecentPlay2, String theaterRecentPlay3) {
        this.theaterId = theaterId;
        this.theaterName = theaterName;
        this.theaterLocation = theaterLocation;
        this.theaterRecentPlay1 = theaterRecentPlay1;
        this.theaterRecentPlay2 = theaterRecentPlay2;
        this.theaterRecentPlay3 = theaterRecentPlay3;
    }
}
