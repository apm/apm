package com.example.apm.controller;

import com.example.apm.dto.TheaterDTO;
import com.example.apm.entity.Theater;
import com.example.apm.service.TheaterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/theater")
public class TheaterController {

    private final TheaterService theaterService;

    public TheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    @GetMapping("/list")
    public ResponseEntity<Page<TheaterDTO>> getTheaterList(Pageable pageable) {
        Page<TheaterDTO> theaterList = theaterService.getTheaterList(pageable).map(TheaterDTO::from);
        return ResponseEntity.ok(theaterList);
    } //전체 극장 조회

    @GetMapping("/list/seoul")
    public ResponseEntity<Page<TheaterDTO>> getSeoulTheaterList(Pageable pageable) {
        Page<TheaterDTO> seoulTheaterList = theaterService.getSeoulTheaterList(pageable).map(TheaterDTO::from);
        return ResponseEntity.ok(seoulTheaterList);
    } //서울 극장 조회

    @GetMapping("/{theaterId}")
    public ResponseEntity<TheaterDTO> getTheater(@PathVariable("theaterId") Integer theaterId) {
        Theater theater = theaterService.getTheater(theaterId);
        if (theater != null) {
            TheaterDTO theaterDTO = TheaterDTO.from(theater);
            return ResponseEntity.ok(theaterDTO);
        } else {
            return ResponseEntity.notFound().build();
        } //http://localhost:8080/theater/{theaterId}
    }
}
