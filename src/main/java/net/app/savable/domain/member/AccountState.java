package net.app.savable.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountState {
    ACTIVE("ACTIVE", "활성화"),
    INACTIVE("INACTIVE", "비활성화"),
    DELETED("DELETED", "삭제");

    private final String key;
    private final String title;
}
