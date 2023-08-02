package com.example.apm.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Actor {

    @Id
    private Integer actorId;

    private String image; // 배우 사진

    @Column(nullable = false)
    private String actorName;

    private String actorJob;

    private String actorRecentPlay1;

    private String actorRecentPlay2;

    private String actorRecentPlay3;

    @ManyToMany
    Set<SiteUser> likeUser; //좋아요 누른 사용자, 사용자가 중복되면 안되기 때문에 set 사용

    public Actor(){

    }

    public Actor(Integer actorId, String image, String actorName, String actorJob, String actorRecentPlay1, String actorRecentPlay2, String actorRecentPlay3) {
        this.actorId = actorId;
        this.image = image;
        this.actorName = actorName;
        this.actorJob = actorJob;
        this.actorRecentPlay1 = actorRecentPlay1;
        this.actorRecentPlay2 = actorRecentPlay2;
        this.actorRecentPlay3 = actorRecentPlay3;
    }
}