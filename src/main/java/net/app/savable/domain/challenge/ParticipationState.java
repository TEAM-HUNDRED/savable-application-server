package net.app.savable.domain.challenge;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ParticipationState {
    SUCCESS("SUCCESS", "성공"),
    FAIL("FAIL", "실패"),
    IN_PROGRESS("IN_PROGRESS", "진행중");

    private final String key;
    private final String title;
}
