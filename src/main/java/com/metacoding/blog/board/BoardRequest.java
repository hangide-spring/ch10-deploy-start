package com.metacoding.blog.board;

import com.metacoding.blog.user.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// 요청 DTO — Java 21 record: 필드·생성자·getter가 한 줄로 끝나고 세터가 아예 없다
// 작성자는 요청 JSON에 없다 — 토큰에서 꺼낸 사용자가 작성자다
public record BoardRequest(
        @NotBlank(message = "제목은 비어 있을 수 없습니다") @Size(max = 100, message = "제목은 100자 이하여야 합니다") String title,
        @Size(max = 500, message = "내용은 500자 이하여야 합니다") String content) {

    // 변환의 자리 ① — 요청 DTO + 인증된 사용자로 엔티티를 만든다
    public Board toEntity(User user) {
        return Board.builder().title(title).content(content).user(user).build();
    }
}
