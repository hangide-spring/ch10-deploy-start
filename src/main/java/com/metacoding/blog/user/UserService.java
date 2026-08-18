package com.metacoding.blog.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metacoding.blog.ex.Exception400;
import com.metacoding.blog.ex.Exception401;
import com.metacoding.blog.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    // BCrypt만 단독으로 쓴다 — 시큐리티 필터 체인과는 무관하다
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Transactional
    public UserResponse join(JoinRequest request) {
        userRepository.findByUsername(request.username()).ifPresent(u -> {
            throw new Exception400("이미 존재하는 아이디입니다 : " + request.username());
        });
        User user = User.builder()
                .username(request.username())
                .password(encoder.encode(request.password())) // 평문이 아니라 해시를 저장한다
                .build();
        userRepository.save(user); // 선언만 한 UserRepository에 save가 존재한다 — Spring Data JPA가 구현체를 만든다
        System.out.println("회원가입 완료 → id: " + user.getId() + ", 저장된 password: " + user.getPassword());
        return UserResponse.from(user);
    }

    public LoginResponse login(LoginRequest request) {
        // 로그인 처리는 Service가 맡는다 — 대조 성공 시 JWT를 만들어 body로 돌려준다 (사용자 확정)
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new Exception401("아이디 또는 비밀번호가 틀렸습니다"));
        if (!encoder.matches(request.password(), user.getPassword())) {
            throw new Exception401("아이디 또는 비밀번호가 틀렸습니다");
        }
        String accessToken = JwtUtil.create(user);
        System.out.println("로그인 성공 → JWT 발급: " + user.getUsername());
        return new LoginResponse(accessToken);
    }
}
