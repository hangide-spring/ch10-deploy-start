package com.metacoding.blog.user;

import jakarta.validation.constraints.NotBlank;

// 로그인 요청 DTO
public record LoginRequest(
        @NotBlank(message = "아이디는 비어 있을 수 없습니다") String username,
        @NotBlank(message = "비밀번호는 비어 있을 수 없습니다") String password) {
}
