package net.app.savable.global.wrapper;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CustomResponseWrapper extends HttpServletResponseWrapper {
    private final ByteArrayOutputStream capture;
    private ServletOutputStream output;
    private PrintWriter writer;
    private final Map<String, String> headers = new HashMap<>();

    public CustomResponseWrapper(HttpServletResponse response) {
        super(response);
        capture = new ByteArrayOutputStream(response.getBufferSize());
    }

    @Override
    public void addHeader(String name, String value) {
        headers.put(name, value);
        super.addHeader(name, value);
    }

    @Override
    public void setHeader(String name, String value) {
        headers.put(name, value);
        super.setHeader(name, value);
    }

    @Override
    public void setContentType(String type) {
        headers.put("Content-Type", type);
        super.setContentType(type);
    }

    @Override
    public void setCharacterEncoding(String charset) {
        headers.put("Content-Encoding", charset);
        super.setCharacterEncoding(charset);
    }

    // 필요한 경우, 다른 헤더 관련 메서드들도 오버라이드

    @Override
    public String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public Set<String> getHeaderNames() {
        return headers.keySet();
    }

    public Map<String, String> getCapturedHeaders() {
        return headers;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) {
            throw new IllegalStateException("getWriter() has already been called for this response.");
        }

        if (output == null) {
            output = new ServletOutputStream() {
                @Override
                public void write(int b) throws IOException {
                    capture.write(b);
                }

                @Override
                public boolean isReady() {
                    // 이 예제에서는 항상 준비된 것으로 간주
                    return true;
                }

                @Override
                public void setWriteListener(WriteListener writeListener) {
                    // 비동기 쓰기는 이 예제에서 지원하지 않음
                    // 필요한 경우 여기에 구현을 추가
                }
            };
        }

        return output;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (output != null) {
            throw new IllegalStateException("getOutputStream() has already been called for this response.");
        }

        if (writer == null) {
            writer = new PrintWriter(capture);
        }

        return writer;
    }

    public byte[] getCaptureAsBytes() {
        return capture.toByteArray();
    }

    public String getCaptureAsString() {
        return new String(capture.toByteArray());
    }
}
