package net.app.savable.global.config.auth;

import io.sentry.Session;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.global.config.auth.dto.SessionMember;
import net.app.savable.global.error.exception.SessionMemberNotFoundException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("LoginMemberArgumentResolver.supportsParameter() 실행");
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginMember.class) != null; // @LoginMember 어노테이션이 붙어있는지 확인
        boolean isUserClass = SessionMember.class.equals(parameter.getParameterType()); // 파라미터 클래스 타입이 SessionMember.class 인지 확인
        return isLoginUserAnnotation && isUserClass; // true인 경우 세션 재발급.
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception{
        log.info("LoginMemberArgumentResolver.resolveArgument() 실행");
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false); // 세션이 없으면 null 반환

        if (session == null) {
            throw new SessionMemberNotFoundException();
        }

        SessionMember member = (SessionMember) session.getAttribute("member");
        if (member == null) {
            throw new SessionMemberNotFoundException();
        }
        return member;
    }
}
