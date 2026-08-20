package com.metacoding.blog.board;

import java.sql.Timestamp;

// 응답 DTO — API 응답 모양을 테이블 구조에서 분리한다
// 연관관계가 생기면서 응답에 작성자가 추가됐다 — 7차시에 배운 DTO의 필드 확장이다
public record BoardResponse(Integer id, String title, String content, String writer, Timestamp createdAt) {

    // 변환의 자리 ② — 엔티티를 응답 DTO로 감싼다
    public static BoardResponse from(Board board) {
        return new BoardResponse(board.getId(), board.getTitle(), board.getContent(), board.getUser().getUsername(),
                board.getCreatedAt());
    }
}
