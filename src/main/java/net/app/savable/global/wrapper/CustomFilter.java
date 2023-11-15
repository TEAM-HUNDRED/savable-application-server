package net.app.savable.global.wrapper;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomFilter implements Filter {
    // ...

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        CustomResponseWrapper wrappedResponse = new CustomResponseWrapper(httpServletResponse);

        chain.doFilter(request, wrappedResponse);

        // 캡쳐된 응답 헤더 출력
        printResponseHeaders(wrappedResponse);

        // 캡쳐된 응답 본문을 콘솔에 출력
        String responseBody = wrappedResponse.getCaptureAsString();
        System.out.printf("*** response body ***\n");
        System.out.println("Captured Response Body: " + responseBody);

        // 실제 응답에 데이터를 다시 쓰기
        ServletOutputStream out = response.getOutputStream();
        out.write(wrappedResponse.getCaptureAsBytes());
        out.close();
    }

    private void printResponseHeaders(CustomResponseWrapper wrappedResponse) {
        Collection<String> headerNames = wrappedResponse.getHeaderNames();
        System.out.printf("\n*** response headers ***\n");
        StringBuilder responseHeaders = new StringBuilder("Captured Response Headers:\n");
        for (String headerName : headerNames) {
            String headerValue = wrappedResponse.getHeader(headerName);
            responseHeaders.append(headerName).append(": ").append(headerValue).append("\n");
        }
        System.out.println(responseHeaders.toString());
    }

    // ...
}
