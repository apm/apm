package com.example.apm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SiteUser {
    @Id
    @Column(name = "user_name", unique = true) //유일한 값 저장
    private String username;

    private String password;


}