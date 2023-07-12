package com.example.apm.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name="USER_INFO")
@NoArgsConstructor
public class User {
    @Id
    private String userId;
    private String password;

    @Builder
    public User(
            String userId,
            String password) {
        this.userId = userId;
        this.password = password;
    }
}