package com.example.apm.controller;

import com.example.apm.entity.Seat;
import com.example.apm.entity.SiteUser;
import com.example.apm.entity.View;
import com.example.apm.service.SeatService;
import com.example.apm.service.UserService;
import com.example.apm.service.ViewService;
import lombok.RequiredArgsConstructor;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/view")
public class ViewController {
    @Autowired
    private final ViewService viewService;
    @Autowired
    private final SeatService seatService;
    @Autowired
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write/{seatId}")
    public String CreateView(Model model, @PathVariable("seatId") Integer seatId, @RequestParam String comment,
                             @RequestParam int seatScore, Principal principal){ //로그인한 사용자 알기위해 principal 사용. 로그인을 해야 생성되는 객체
        Seat seat = this.seatService.getSeat(seatId); //시야 정보를 저장
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.viewService.create(seat, comment, seatScore, siteUser);
        return String.format("redirect:/seat/%s", seatId);
    }
}
