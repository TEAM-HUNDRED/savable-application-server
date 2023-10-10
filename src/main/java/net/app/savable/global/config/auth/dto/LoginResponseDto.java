package net.app.savable.global.config.auth.dto;

import lombok.Data;
import net.app.savable.domain.member.Member;

@Data
public class LoginResponseDto {
    public boolean loginSuccess;
    public Member account;
}
