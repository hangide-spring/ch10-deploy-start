package com.metacoding.blog.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect // 공통 관심사(요청·응답 로깅)를 핵심 로직에서 분리해 한 곳에 모은 클래스
@Component
public class LogAspect {

    // 포인트컷 — 모든 컨트롤러의 모든 메서드 앞뒤에 이 코드가 끼워 넣어진다
    // 컨테이너가 대상 객체를 프록시로 감싸기에 가능하다 (2차시 미니 컨테이너의 연장선)
    @Around("execution(* com.metacoding.blog..*Controller.*(..))")
    public Object logRequestResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("요청 → {}", joinPoint.getSignature().toShortString());
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed(); // 실제 컨트롤러 메서드 실행
        log.info("응답 ← {} ({}ms)", joinPoint.getSignature().toShortString(), System.currentTimeMillis() - start);
        return result;
    }
}
