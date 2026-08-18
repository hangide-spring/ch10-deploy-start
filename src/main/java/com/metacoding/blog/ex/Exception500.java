package com.metacoding.blog.ex;

// 의도적으로 던지는 서버 오류 — 예외 이름이 곧 HTTP 상태코드다
public class Exception500 extends RuntimeException {

    public Exception500(String msg) {
        super(msg);
    }
}
