package net.app.savable.global.wrapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;


@Component
public class CustomInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        // 응답 헤더 캡쳐 및 출력
        Collection<String> headerNames = response.getHeaderNames();
        StringBuilder responseHeaders = new StringBuilder("Captured Response Headers:\n");
        for (String headerName : headerNames) {
            String headerValue = response.getHeader(headerName);
            responseHeaders.append(headerName).append(": ").append(headerValue).append("\n");
        }
        System.out.println(responseHeaders.toString());
    }
}
