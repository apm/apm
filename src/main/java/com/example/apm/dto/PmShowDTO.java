package com.example.apm.dto;

import com.example.apm.entity.PmShow;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PmShowDTO {
    private String pmShowId;
    private String title;
    private String showDate;
    private String genre;
    private String runningTime;
    private String synopsis;
    private String poster;
    private TheaterDTO theater;

    public static PmShowDTO from(PmShow pmShow) {
        PmShowDTO pmShowDTO = new PmShowDTO();
        pmShowDTO.setPmShowId(pmShow.getPmShowId());
        pmShowDTO.setTitle(pmShow.getTitle());
        pmShowDTO.setShowDate(pmShow.getShowDate());
        pmShowDTO.setGenre(pmShow.getGenre());
        pmShowDTO.setRunningTime(pmShow.getRunningTime());
        pmShowDTO.setSynopsis(pmShow.getSynopsis());
        pmShowDTO.setPoster(pmShow.getPoster());
        pmShowDTO.setTheater(TheaterDTO.from(pmShow.getTheater()));
        return pmShowDTO;
    }
}
