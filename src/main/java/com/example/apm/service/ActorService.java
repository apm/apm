package com.example.apm.service;

import com.example.apm.entity.Actor;
import com.example.apm.entity.SiteUser;
import com.example.apm.repository.ActorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ActorService {
    private final ActorRepository actorRepository;
    public void like(Actor actor, SiteUser siteUser){
        actor.getLikeUser().add(siteUser);
        this.actorRepository.save(actor);
    } // actor 엔티티에 user를 likeUser로 저장하는 like메소드
}
