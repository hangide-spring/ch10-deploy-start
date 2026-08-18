package com.metacoding.blog.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// 회원가입 요청 DTO — 7차시 패턴 그대로 record
public record JoinRequest(
        @NotBlank(message = "아이디는 비어 있을 수 없습니다") @Size(max = 20, message = "아이디는 20자 이하여야 합니다") String username,
        @NotBlank(message = "비밀번호는 비어 있을 수 없습니다") String password) {
}
