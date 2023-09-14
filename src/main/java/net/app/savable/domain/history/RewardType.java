package net.app.savable.domain.history;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RewardType {
    GIFTCARD("GIFTCARD", "기프티콘"),
    CHALLENGE("CHALLENGE", "챌린지");

    private final String key;
    private final String title;
}
