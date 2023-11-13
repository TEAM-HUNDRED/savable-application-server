package net.app.savable.global.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.global.config.auth.LoginMember;
import net.app.savable.global.config.auth.dto.SessionMember;
import net.app.savable.global.error.exception.SessionMemberNotFoundException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

@Slf4j
public class SessionMemberInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("SessionMemberInterceptor.preHandle() 실행");
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();

            if (method.isAnnotationPresent(LoginMember.class)) { // @LoginMember 어노테이션이 붙어있는지 확인
                HttpSession session = request.getSession(false); // 세션이 없으면 null 반환
                if (session == null) {
                    throw new SessionMemberNotFoundException();
                } else {
                    SessionMember member = (SessionMember) session.getAttribute("member");
                    if (member == null) {
                        throw new SessionMemberNotFoundException();
                    }
                }
            }
        }
        return true;
    }
}
