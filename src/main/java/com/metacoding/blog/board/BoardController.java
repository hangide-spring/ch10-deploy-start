package com.metacoding.blog.board;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.metacoding.blog.util.Resp;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/boards")
    public ResponseEntity<?> list() {
        System.out.println("GET /boards 요청 → 목록 JSON 응답");
        return ResponseEntity.ok(Resp.ok(boardService.findAll()));
    }

    @GetMapping("/boards/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") int id) {
        System.out.println("GET /boards/" + id + " 요청 → 상세 JSON 응답");
        return ResponseEntity.ok(Resp.ok(boardService.findById(id)));
    }

    @PostMapping("/boards")
    public ResponseEntity<?> save(@Valid @RequestBody BoardRequest request, HttpServletRequest req) {
        // 필터가 검증한 토큰에서 꺼내 전달해 준 사용자 — 여기 도달했다는 것은 인증이 통과됐다는 뜻이다
        Integer userId = (Integer) req.getAttribute("userId");
        System.out.println("POST /boards 요청 → title: " + request.title() + ", userId: " + userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(Resp.created(boardService.save(request, userId)));
    }

    @PutMapping("/boards/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @Valid @RequestBody BoardRequest request,
            HttpServletRequest req) {
        Integer userId = (Integer) req.getAttribute("userId");
        System.out.println("PUT /boards/" + id + " 요청 → userId: " + userId);
        return ResponseEntity.ok(Resp.ok(boardService.update(id, request, userId)));
    }

    @DeleteMapping("/boards/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id, HttpServletRequest req) {
        Integer userId = (Integer) req.getAttribute("userId");
        System.out.println("DELETE /boards/" + id + " 요청 → userId: " + userId);
        boardService.delete(id, userId);
        return ResponseEntity.ok(Resp.ok(null));
    }
}
