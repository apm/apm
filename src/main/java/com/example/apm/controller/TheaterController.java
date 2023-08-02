package com.example.apm.controller;

import com.example.apm.entity.Seat;
import com.example.apm.entity.Theater;
import com.example.apm.entity.View;
import com.example.apm.service.SeatService;
import com.example.apm.service.TheaterService;
import com.example.apm.service.ViewService;
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
@RequestMapping("/theater")
public class TheaterController {
    @Autowired
    private final TheaterService theaterService;
    @Autowired
    private final SeatService seatService;
    @Autowired
    private final ViewService viewService;

    @GetMapping("/list") // 전체 극장 조회
    public String list(Model model){
        List<Theater> theaterList = this.theaterService.getTheaterList();
        model.addAttribute("theaterList", theaterList);
        return "theater_list";
    }

    @GetMapping(value = "/{theaterId}") // 특정 극장 조회
    public String detail(Model model, @PathVariable("theaterId") Integer theaterId) {
        Theater theater = this.theaterService.getTheater(theaterId);
        List<Seat> seatList = seatService.getSeatsByTheaterId(theaterId);

        model.addAttribute("theater", theater);
        model.addAttribute("seatList", seatList);

        return "theater_detail";
    }
}
