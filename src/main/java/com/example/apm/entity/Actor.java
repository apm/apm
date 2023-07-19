package com.example.apm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer actorId;

    @Column(nullable = false)
    private String actorName;

    private String image; // 배우 사진

    @ManyToMany
    Set<SiteUser> likeUser; //좋아요 누른 사용자, 사용자가 중복되면 안되기 때문에 set 사용
}
//actorId, actorName, image

//하고있는 공연 연결시키려면 스케줄 테이블을 생성해놔야 가능할듯