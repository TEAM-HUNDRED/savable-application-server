package net.app.savable.domain.challenge.dto;

import lombok.Getter;
import lombok.ToString;
import net.app.savable.domain.challenge.Challenge;

import java.time.LocalDate;

@ToString
@Getter
public class ParticipatableChallengeDto {
    private Long id;
    private String image;
    private String title;
    private Boolean hasDeadline;
    private LocalDate startDate;
    private LocalDate endDate;

    public ParticipatableChallengeDto(Challenge challenge) {
        this.id = challenge.getId();
        this.image = challenge.getImage();
        this.title = challenge.getTitle();
        this.hasDeadline = challenge.getHasDeadline();
        this.startDate = challenge.getStartDate();
        this.endDate = challenge.getEndDate();
    }
}
