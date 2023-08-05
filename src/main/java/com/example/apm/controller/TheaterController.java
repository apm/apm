package com.example.apm.controller;

import com.example.apm.entity.Seat;
import com.example.apm.entity.Theater;
import com.example.apm.entity.View;
import com.example.apm.service.SeatService;
import com.example.apm.service.TheaterService;
import com.example.apm.service.ViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/theater")
public class TheaterController {
    @Autowired
    private final TheaterService theaterService;
    @Autowired
    private final SeatService seatService;
    @Autowired
    private final ViewService viewService;

    @GetMapping("/list") // 전체 극장 조회
    public Page<Theater> list(Model model, @RequestParam(value = "page", defaultValue = "0") int page){
        return theaterService.getTheaterList(page);
    }

//    @GetMapping("/list") // 전체 극장 조회
//    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page){
//        Page<Theater> paging = this.theaterService.getTheaterList(page);
//        model.addAttribute("paging", paging);
//        return "theater_list";
//    }
//
//    @GetMapping(value = "/{theaterId}") // 특정 극장 조회
//    public String detail(Model model, @PathVariable("theaterId") Integer theaterId) {
//        Theater theater = this.theaterService.getTheater(theaterId);
//        List<Seat> seatList = seatService.getSeatsByTheaterId(theaterId);
//
//        model.addAttribute("theater", theater);
//        model.addAttribute("seatList", seatList);
//
//        return "theater_detail";
//    }


}
