package net.app.savable.domain.shop;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SendState {
    WAITING("WAITING", "발송 대기"),
    READY("READY", "발송 준비"),
    COMPLETE("COMPLETE", "발송 완료");

    private final String key;
    private final String title;
}
