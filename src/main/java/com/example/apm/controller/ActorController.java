package com.example.apm.controller;

import com.example.apm.entity.Actor;
import com.example.apm.repository.ActorRepository;
import com.example.apm.service.ActorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/actor")
public class ActorController {
    @Autowired
    private final ActorService actorService;
    @GetMapping("/list")
    public String actorInquiry(Model model){
        List<Actor> actorList = this.actorService.getList(); //전체 배우 조회 -> 모델 객체를 통해 뷰로 전달
        model.addAttribute("actorList", actorList);
        return "actor_list";
    }

    @GetMapping(value = "/detail/{actorId}")
    public String detail(Model model, @PathVariable("actorId") Integer actorId){
        Actor actor = this.actorService.getActor(actorId);
        model.addAttribute("actor", actor);
        return "actor_detail";
    }
}
