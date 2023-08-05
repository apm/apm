package com.example.apm.controller;

import com.example.apm.entity.Actor;
import com.example.apm.entity.Theater;
import com.example.apm.repository.ActorRepository;
import com.example.apm.service.ActorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/actor")
public class ActorController {
    @Autowired
    private final ActorService actorService;

    @GetMapping("/list") // 전체 극장 조회
    public Page<Actor> list(Model model, @RequestParam(value = "page", defaultValue = "0") int page){
        return actorService.getActorList(page);
    }

//    @GetMapping("/list")
//    public String actorInquiry(Model model, @RequestParam(value = "page", defaultValue = "0") int page){
//        Page<Actor> paging = this.actorService.getActorList(page);
//        model.addAttribute("paging", paging);
//        return "actor_list";
//    } // 전체 배우 조회 + 페이징

    @GetMapping(value = "/{actorId}")
    public String detail(Model model, @PathVariable("actorId") Integer actorId){
        Actor actor = this.actorService.getActor(actorId);
        model.addAttribute("actor", actor);
        return "actor_detail";
    }
}
