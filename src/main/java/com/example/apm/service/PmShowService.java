package com.example.apm.service;

import com.example.apm.DataNotFoundException;
import com.example.apm.entity.PmShow;
import com.example.apm.repository.PmShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class PmShowService {

    private final PmShowRepository pmShowRepository;

    public PmShowService(PmShowRepository pmShowRepository) {
        this.pmShowRepository = pmShowRepository;
    }

    public PmShow getPmShow(String pmShowId) {
        Optional<PmShow> pmShow = this.pmShowRepository.findById(pmShowId);
        if (pmShow.isPresent()) {
            return pmShow.get();
        } else {
            throw new DataNotFoundException("공연 정보가 없습니다.");
        }
    }

    public Page<PmShow> getPmShowList(Pageable pageable) {
        return pmShowRepository.findAll(pageable);
    }
}
