package com.metacoding.blog.ex;

// 유효성 검사 실패 — 예외 이름이 곧 HTTP 상태코드다
public class Exception400 extends RuntimeException {

    public Exception400(String msg) {
        super(msg);
    }
}
