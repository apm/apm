package com.example.apm.controller;

import com.example.apm.dto.ActorDTO;
import com.example.apm.dto.TheaterDTO;
import com.example.apm.entity.Actor;
import com.example.apm.entity.Theater;
import com.example.apm.service.ActorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/actor")
public class ActorController {

    private final ActorService actorService;

    @GetMapping("/list")
    public ResponseEntity<Page<Actor>> actorInquiry(@RequestParam(value = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("actorName").ascending());
        Page<Actor> paging = actorService.getActorList(pageable);
        return ResponseEntity.ok(paging);
    } //http://localhost:8080/actor/list?page=1

    @GetMapping("/{actorId}")
    public ResponseEntity<ActorDTO> getTheater(@PathVariable("actorId") Integer actorId) {
        Actor actor = actorService.getActor(actorId);
        if (actor != null) {
            ActorDTO actorDTO = ActorDTO.from(actor);
            return ResponseEntity.ok(actorDTO);
        } else {
            return ResponseEntity.notFound().build();
        } //http://localhost:8080/actor/{actorId}
    }
}
