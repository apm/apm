package com.example.apm.service;

import com.example.apm.OrderingByKoreanEnglishNumberSpecial;
import com.example.apm.entity.Theater;
import com.example.apm.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TheaterService {

    private final TheaterRepository theaterRepository;

    public Theater getTheater(Integer theaterId) {
        return theaterRepository.findById(theaterId).orElse(null);
    }

    public Page<Theater> getTheaterList(Pageable pageable) {
        List<Theater> allTheaters = theaterRepository.findAll();
        List<Theater> sortedTheaters = allTheaters.stream()
                .sorted((t1, t2) -> OrderingByKoreanEnglishNumberSpecial.compare(
                        t1.getTheaterName(), t2.getTheaterName()))
                .collect(Collectors.toList());

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Theater> pageTheaters;

        if (sortedTheaters.size() < startItem) {
            pageTheaters = List.of();
        } else {
            int toIndex = Math.min(startItem + pageSize, sortedTheaters.size());
            pageTheaters = sortedTheaters.subList(startItem, toIndex);
        }

        return new PageImpl<>(pageTheaters, pageable, sortedTheaters.size());
    }

    public Page<Theater> getSeoulTheaterList(Pageable pageable) {
        Page<Theater> allTheaters = getTheaterList(pageable);
        List<Theater> seoulTheaters = allTheaters.getContent()
                .stream()
                .filter(theater -> theater.getTheaterLocation().startsWith("서울"))
                .collect(Collectors.toList());
        return new PageImpl<>(seoulTheaters, pageable, seoulTheaters.size());
    }
}