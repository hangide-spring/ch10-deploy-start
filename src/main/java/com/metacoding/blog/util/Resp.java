package com.metacoding.blog.util;

// 공통 응답 형식 — 성공도 실패도 같은 뼈대(status·msg·body)로 내려간다 (9차시 도입)
public record Resp<T>(Integer status, String msg, T body) {

    public static <T> Resp<T> ok(T body) {
        return new Resp<>(200, "성공", body);
    }

    public static <T> Resp<T> created(T body) {
        return new Resp<>(201, "성공", body);
    }

    public static Resp<Void> fail(Integer status, String msg) {
        return new Resp<>(status, msg, null);
    }
}
