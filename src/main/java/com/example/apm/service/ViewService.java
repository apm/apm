package com.example.apm.service;

import com.example.apm.DataNotFoundException;
import com.example.apm.entity.Seat;
import com.example.apm.entity.SiteUser;
import com.example.apm.entity.Theater;
import com.example.apm.entity.View;
import com.example.apm.repository.ViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ViewService {
    private final ViewRepository viewRepository;
    private final SeatService seatService;

    public List<View> getViewList() {
        return this.viewRepository.findAll();
    }

    public View getView(Integer viewId) {
        Optional<View> view = this.viewRepository.findByViewId(viewId);
        if (view.isPresent()) {
            return view.get();
        } else {
            throw new DataNotFoundException("시야 정보가 없습니다.");
        }
    }

    public void create(Seat seat, String comment, int seatScore, SiteUser writer){
        View view = new View();
        view.setComment(comment);
        view.setSeatScore(seatScore);
        view.setSeat(seat);
        view.setWriter(writer); //뷰 저장시 작성자 저장됨
        this.viewRepository.save(view);
    } // 시야 정보 생성

}
