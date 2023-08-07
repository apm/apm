package com.example.apm.controller;

import com.example.apm.dto.PmShowDTO;
import com.example.apm.entity.PmShow;
import com.example.apm.service.PmShowService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pmshow")
public class PmShowController {

    private final PmShowService pmShowService;

    public PmShowController(PmShowService pmShowService) {
        this.pmShowService = pmShowService;
    }

    @GetMapping("/list")
    public ResponseEntity<Page<PmShowDTO>> getPmShowList(@RequestParam(value = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("title").ascending());
        Page<PmShow> pmShowPage = pmShowService.getPmShowList(pageable);
        Page<PmShowDTO> pmShowDTOPage = pmShowPage.map(PmShowDTO::from);
        return ResponseEntity.ok(pmShowDTOPage);
    } //http://localhost:8080/pmshow/list?page=1 전체 공연 조회

    @GetMapping("/{pmShowId}")
    public ResponseEntity<PmShowDTO> getPmShowDetail(@PathVariable("pmShowId") String pmShowId) {
        PmShow pmShow = pmShowService.getPmShow(pmShowId);
        if (pmShow != null) {
            PmShowDTO pmShowDTO = PmShowDTO.from(pmShow);
            return ResponseEntity.ok(pmShowDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    } //http://localhost:8080/pmshow/{pmShowId} 특정 공연 조회
}
