package com.example.apm.service;

import com.example.apm.DataNotFoundException;
import com.example.apm.entity.Actor;
import com.example.apm.entity.SiteUser;
import com.example.apm.entity.Theater;
import com.example.apm.repository.ActorRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.sql.Time;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class ActorService {
    private final ActorRepository actorRepository;

    public Page<Actor> getActorList(Pageable pageable) {
        return actorRepository.findAll(pageable);
    }

    public Actor getActor(Integer actorId) {
        Optional<Actor> actor = this.actorRepository.findByActorId(actorId);
        if (actor.isPresent()) {
            return actor.get();
        } else {
            throw new DataNotFoundException("배우 정보가 없습니다.");
        }
    }





}
