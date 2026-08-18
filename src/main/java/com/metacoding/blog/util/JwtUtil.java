package com.metacoding.blog.util;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.metacoding.blog.user.User;

public class JwtUtil {

    // 서명용 비밀 키 — 교육용 하드코딩이다. 실무에서는 코드가 아니라 설정·환경변수로 관리한다
    private static final String SECRET = "metacoding-blog-secret";
    private static final long EXP = 1000L * 60 * 60; // 1시간

    public static String create(User user) {
        return JWT.create()
                .withSubject("blog")
                .withExpiresAt(new Date(System.currentTimeMillis() + EXP))
                .withClaim("id", user.getId()) // payload는 누구나 읽을 수 있다 — 민감정보 금지
                .withClaim("username", user.getUsername())
                .sign(Algorithm.HMAC512(SECRET)); // 서명 — 위조 판별용이지 암호화가 아니다
    }

    public static DecodedJWT verify(String token) {
        // 서명이 다르거나 만료됐으면 JWTVerificationException이 던져진다
        return JWT.require(Algorithm.HMAC512(SECRET)).build().verify(token);
    }
}
