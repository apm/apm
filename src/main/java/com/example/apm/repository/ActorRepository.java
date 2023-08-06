package com.example.apm.repository;

import com.example.apm.entity.Actor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActorRepository extends JpaRepository<Actor, Integer> {
    Actor findByActorName(String actorName); //배우 이름 조회
    List<Actor> findAll();
    Optional<Actor> findByActorId(Integer actorId);
    Page<Actor> findAll(Pageable pageable); //페이징처리

}
