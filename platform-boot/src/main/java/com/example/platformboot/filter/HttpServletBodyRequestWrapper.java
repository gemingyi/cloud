package com.example.platformboot.filter;

import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2022/1/21 18:01
 */
public class HttpServletBodyRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] bodyByte;
    private final String bodyString;

    public HttpServletBodyRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.bodyString = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        this.bodyByte = this.bodyString.getBytes(StandardCharsets.UTF_8);
    }

    public String getBodyString() {
        return this.bodyString;
    }

    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.bodyByte);
        return new ServletInputStream() {
            public boolean isFinished() {
                return false;
            }

            public boolean isReady() {
                return false;
            }

            public void setReadListener(ReadListener readListener) {}

            public int read() {
                return byteArrayInputStream.read();
            }

        };
    }

}
