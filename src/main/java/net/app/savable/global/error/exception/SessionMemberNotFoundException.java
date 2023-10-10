package net.app.savable.global.error.exception;

public class SessionMemberNotFoundException extends RuntimeException{
    public SessionMemberNotFoundException() {
        super(ErrorCode.SESSION_MEMBER_NOT_FOUND.getMessage());
    }
}
