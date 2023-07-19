package com.example.apm.service;

import com.example.apm.DataNotFoundException;
import com.example.apm.entity.Actor;
import com.example.apm.entity.SiteUser;
import com.example.apm.repository.ActorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ActorService {
    private final ActorRepository actorRepository;
    public void like(Actor actor, SiteUser siteUser){
        actor.getLikeUser().add(siteUser);
        this.actorRepository.save(actor);
    } // actor 엔티티에 user를 likeUser로 저장하는 like메소드

    public List<Actor> getList(){
        return this.actorRepository.findAll();
    }

    public Actor getActor(Integer actorId){
        Optional<Actor> actor = this.actorRepository.findByActorId(actorId);
        if (actor.isPresent()){
            return actor.get();
        } else {
            throw new DataNotFoundException("actor not found");
        }
    }
}
