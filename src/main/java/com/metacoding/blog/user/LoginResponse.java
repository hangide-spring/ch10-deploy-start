package com.metacoding.blog.user;

// 로그인 응답 DTO — 발급한 JWT를 응답 body로 내려 준다 (사용자 확정)
public record LoginResponse(String accessToken) {
}
