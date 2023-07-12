package com.example.apm.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("/hello")
    public String index(){
        return "hello world!";
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/hello";
    } //시작페이지 임시설정
}
