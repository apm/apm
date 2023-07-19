package com.example.apm.controller;

import com.example.apm.entity.Actor;
import com.example.apm.repository.ActorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/actor")
public class ActorController {
    @Autowired
    private final ActorRepository actorRepository;
    @GetMapping("/list")
    public String actorInquiry(Model model){
        List<Actor> actorList = actorRepository.findAll(); //전체 배우 조회 -> 모델 객체를 통해 뷰로 전달
        model.addAttribute("actorList", actorList);
        return "actor_inquiry";
    }

    @GetMapping("/actorName")
    public String actorName(){
        return "actor_name";
    }
}
