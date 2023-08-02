package com.example.apm.entity;

import java.time.LocalDate;

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
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ScheduleId;

    @ManyToOne
    @JoinColumn(name = "actorId") // 외래키
    private Actor actor;

    @ManyToOne
    @JoinColumn(name = "pmShowId") // 외래키
    private PmShow pmShow;

    @Column(nullable = false)
    private LocalDate Date;
    // 연월일까지만 포함 / 시간 필드 생성여부에 따라 LocalDateTime 사용
}
