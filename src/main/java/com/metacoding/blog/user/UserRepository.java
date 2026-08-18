package com.metacoding.blog.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

// 이번에는 도구를 쓴다 — 선언 한 줄로 save·findById 등이 생긴다 (Spring Data JPA)
// EntityManager로 손수 만든 BoardRepository와 비교해 보라
public interface UserRepository extends JpaRepository<User, Integer> {

    // 메서드 이름을 규칙대로 지으면 구현 없이 쿼리가 만들어진다
    Optional<User> findByUsername(String username);
}
