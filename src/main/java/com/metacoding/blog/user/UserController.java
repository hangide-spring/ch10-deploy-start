package com.metacoding.blog.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.metacoding.blog.util.Resp;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<Resp<UserResponse>> join(@Valid @RequestBody JoinRequest request) {
        System.out.println("POST /join 요청 → username: " + request.username());
        return ResponseEntity.status(HttpStatus.CREATED).body(Resp.created(userService.join(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<Resp<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        System.out.println("POST /login 요청 → username: " + request.username());
        return ResponseEntity.ok(Resp.ok(userService.login(request)));
    }
}
