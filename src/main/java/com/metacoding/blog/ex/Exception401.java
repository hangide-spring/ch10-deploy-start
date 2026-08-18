package com.metacoding.blog.ex;

// 인증 실패 — 예외 이름이 곧 HTTP 상태코드다
public class Exception401 extends RuntimeException {

    public Exception401(String msg) {
        super(msg);
    }
}
