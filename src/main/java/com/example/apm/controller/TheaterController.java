package com.example.apm.controller;

import com.example.apm.dto.TheaterDTO;
import com.example.apm.entity.Theater;
import com.example.apm.service.TheaterService;
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
    public ResponseEntity<List<TheaterDTO>> getAllTheaters(@RequestParam(value = "page", defaultValue = "0") int page) {
        int size = 10; // 페이지 당 조회할 개수
        Sort sort = Sort.by("theaterName").ascending(); // theaterName을 기준으로 오름차순 정렬
        PageRequest pageable = PageRequest.of(page, size, sort);

        Page<Theater> theaterPage = theaterService.getTheaterList(pageable);
        List<TheaterDTO> theaterDTOList = theaterPage.getContent().stream()
                .map(TheaterDTO::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(theaterDTOList);
    } //http://localhost:8080/theater/list?page=1
}
