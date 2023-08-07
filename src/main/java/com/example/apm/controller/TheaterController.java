package com.example.apm.controller;

import com.example.apm.dto.TheaterDTO;
import com.example.apm.entity.Theater;
import com.example.apm.service.TheaterService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/theater")
public class TheaterController {

    private final TheaterService theaterService;

    public TheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

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

    @GetMapping("/list")
    public ResponseEntity<List<TheaterDTO>> getAllTheaters(Pageable pageable) {
        List<Theater> allTheaters = theaterService.getTheaterList();

        // 서울이 포함된 극장만 필터링하여 TheaterDTO로 변환
        List<TheaterDTO> theaterDTOList = allTheaters.stream()
                .filter(theater -> theater.getTheaterLocation().contains("서울"))
                .map(TheaterDTO::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(theaterDTOList);


    } //http://localhost:8080/theater/list?page=1
}
