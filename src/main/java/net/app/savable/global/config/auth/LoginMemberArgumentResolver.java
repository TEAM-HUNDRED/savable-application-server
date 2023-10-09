package net.app.savable.global.config.auth;

import io.sentry.Session;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import net.app.savable.global.config.auth.dto.SessionMember;
import net.app.savable.global.error.exception.SessionMemberNotFoundException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final HttpSession httpSession;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginMember.class) != null; // @LoginMember 어노테이션이 붙어있는지 확인
        boolean isUserClass = SessionMember.class.equals(parameter.getParameterType()); // 파라미터 클래스 타입이 SessionMember.class 인지 확인
        return isLoginUserAnnotation && isUserClass;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception{
        SessionMember member = (SessionMember) httpSession.getAttribute("member");
        if (member == null) {
            throw new SessionMemberNotFoundException();
        }
        return member;
    }
}
