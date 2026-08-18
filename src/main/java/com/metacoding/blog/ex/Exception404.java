package com.metacoding.blog.ex;

// 리소스를 찾을 수 없음 — 예외 이름이 곧 HTTP 상태코드다
public class Exception404 extends RuntimeException {

    public Exception404(String msg) {
        super(msg);
    }
}
