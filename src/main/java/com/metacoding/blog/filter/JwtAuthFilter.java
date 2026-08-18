package com.metacoding.blog.filter;

import java.io.IOException;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import tools.jackson.databind.ObjectMapper; // 스프링 부트 4는 Jackson 3 — 패키지가 tools.jackson 으로 바뀌었다
import com.metacoding.blog.ex.Exception401;
import com.metacoding.blog.util.JwtUtil;
import com.metacoding.blog.util.Resp;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 서블릿 필터 — 톰캣과 DispatcherServlet 사이의 관문. 여기서 거부되면 컨트롤러는 실행조차 되지 않는다
public class JwtAuthFilter implements Filter {

    // 필터는 스프링 MVC 바깥이라 Jackson 자동 직렬화가 없다 — 직접 JSON을 만든다
    private final ObjectMapper om = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        // 조회(GET)는 로그인 없이 공개다 — 등록·수정·삭제만 보호한다
        if ("GET".equals(request.getMethod())) {
            chain.doFilter(req, resp);
            return;
        }

        System.out.println("JWT 필터 동작 → " + request.getMethod() + " " + request.getRequestURI());

        // 필터 안의 예외는 @RestControllerAdvice가 못 잡는다(DispatcherServlet 앞이다)
        // 그래서 여기서 Exception401을 던지고, 아래 catch가 직접 Resp 형식으로 응답을 쓴다
        try {
            String header = request.getHeader("Authorization");
            if (header == null || !header.startsWith("Bearer ")) {
                throw new Exception401("인증되지 않았습니다 — 토큰이 없습니다");
            }
            String token = header.replace("Bearer ", "");
            DecodedJWT decoded = JwtUtil.verify(token);
            Integer userId = decoded.getClaim("id").asInt();
            request.setAttribute("userId", userId); // 검증된 사용자 정보를 컨트롤러에 전달한다
            System.out.println("토큰 검증 성공 → userId: " + userId);
            chain.doFilter(req, resp);
        } catch (Exception401 e) {
            fail(response, e.getMessage());
        } catch (JWTVerificationException e) {
            fail(response, "인증되지 않았습니다 — 유효하지 않은 토큰입니다");
        }
    }

    private void fail(HttpServletResponse response, String msg) throws IOException {
        System.out.println("토큰 검증 실패 → 401 응답 (컨트롤러 미실행)");
        response.setStatus(401);
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(om.writeValueAsString(Resp.fail(401, msg))); // Advice와 같은 Resp 형식
    }
}
