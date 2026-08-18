package com.metacoding.blog.user;

// 회원가입 응답 DTO — 비밀번호(해시)는 절대 응답에 싣지 않는다
public record UserResponse(Integer id, String username) {

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getUsername());
    }
}
