package com.example.apm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
public class MainController {
    @GetMapping("/hello")
    public String index(){
        return "hello world!";
    }

    @GetMapping("/")
    public String root() {
        return "main";
    } //시작페이지 임시설정
}
