package com.example.apm.controller;

import com.example.apm.entity.Seat;
import com.example.apm.entity.View;
import com.example.apm.service.SeatService;
import com.example.apm.service.ViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/seat")
public class SeatController {
    @Autowired
    private final SeatService seatService;
    @Autowired
    private final ViewService viewService;

    @GetMapping(value = "/{seatId}") //특정 좌석 조회
    public String detail(Model model, @PathVariable("seatId") Integer seatId) {
        Seat seat = this.seatService.getSeat(seatId);
        model.addAttribute("seat", seat);
        return "seat_detail";
    }
}
