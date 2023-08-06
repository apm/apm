package com.example.apm.dto;

import com.example.apm.entity.Actor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActorDTO {
    private Integer actorId;
    private String image;
    private String actorName;
    private String actorJob;
    private String actorRecentPlay1;
    private String actorRecentPlay2;
    private String actorRecentPlay3;

    public static ActorDTO from(Actor actor) {
        ActorDTO actorDTO = new ActorDTO();
        actorDTO.setActorId(actor.getActorId());
        actorDTO.setImage(actor.getImage());
        actorDTO.setActorName(actor.getActorName());
        actorDTO.setActorJob(actor.getActorJob());
        actorDTO.setActorRecentPlay1(actor.getActorRecentPlay1());
        actorDTO.setActorRecentPlay2(actor.getActorRecentPlay2());
        actorDTO.setActorRecentPlay3(actor.getActorRecentPlay3());
        return actorDTO;
    }
}
