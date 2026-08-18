package com.metacoding.blog.ex;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.metacoding.blog.util.Resp;

import lombok.extern.slf4j.Slf4j;

@Slf4j
// 어느 컨트롤러에서 던져진 예외든 DispatcherServlet까지 올라와 여기서 잡힌다
// (3차시 프론트 컨트롤러 구조 위에서 동작한다 — 단, 필터 안의 예외는 여기 못 온다)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception400.class)
    public ResponseEntity<Resp<Void>> ex400(Exception400 e) {
        log.warn("400 유효성 실패 : {}", e.getMessage());
        return ResponseEntity.status(400).body(Resp.fail(400, e.getMessage()));
    }

    // @Valid 실패는 스프링이 던지는 예외라 타입이 다르다 — 여기서 잡아 400 형식으로 통일한다
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Resp<Void>> exValid(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        log.warn("400 검증 실패 : {}", msg);
        return ResponseEntity.status(400).body(Resp.fail(400, msg));
    }

    @ExceptionHandler(Exception401.class)
    public ResponseEntity<Resp<Void>> ex401(Exception401 e) {
        log.warn("401 인증 실패 : {}", e.getMessage());
        return ResponseEntity.status(401).body(Resp.fail(401, e.getMessage()));
    }

    @ExceptionHandler(Exception403.class)
    public ResponseEntity<Resp<Void>> ex403(Exception403 e) {
        log.warn("403 권한 없음 : {}", e.getMessage());
        return ResponseEntity.status(403).body(Resp.fail(403, e.getMessage()));
    }

    @ExceptionHandler(Exception404.class)
    public ResponseEntity<Resp<Void>> ex404(Exception404 e) {
        log.warn("404 리소스 없음 : {}", e.getMessage());
        return ResponseEntity.status(404).body(Resp.fail(404, e.getMessage()));
    }

    @ExceptionHandler(Exception500.class)
    public ResponseEntity<Resp<Void>> ex500(Exception500 e) {
        log.error("500 서버 오류 : {}", e.getMessage());
        return ResponseEntity.status(500).body(Resp.fail(500, e.getMessage()));
    }

    // 최종 안전망 — 미처 예상 못 한 예외(진짜 버그)는 전부 여기서 500으로 받는다
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Resp<Void>> exUnknown(Exception e) {
        log.error("알 수 없는 오류", e); // 스택트레이스는 로그로만 — 클라이언트에 노출하지 않는다
        return ResponseEntity.status(500).body(Resp.fail(500, "관리자에게 문의해 주세요"));
    }
}
