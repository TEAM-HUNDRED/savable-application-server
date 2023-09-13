package net.app.savable.domain.challenge;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VerificationState {
    SUCCESS("SUCCESS", "인증 성공"),
    FAIL("FAIL", "인증 실패"),
    WAITING("WAITING", "인증 대기");

    private final String key;
    private final String title;
}
