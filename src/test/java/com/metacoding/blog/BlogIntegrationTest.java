package com.metacoding.blog;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc; // 스프링 부트 4의 새 패키지 위치
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.jayway.jsonpath.JsonPath;

// 통합 테스트 — 슬라이스와 달리 모든 계층(필터 → 컨트롤러 → 서비스 → 리포지토리 → DB)을 실제로 연결한다
@Transactional // 테스트마다 롤백 — 서로 데이터 간섭이 없다
@AutoConfigureMockMvc // 전체 컨테이너 위에 MockMvc를 얹는다 (등록된 서블릿 필터까지 함께 동작)
@SpringBootTest
public class BlogIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void 인증_포함_전체_흐름_test() throws Exception {
        // 1. 회원가입
        mvc.perform(post("/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"username":"integ","password":"1234"}
                        """))
                .andExpect(status().isCreated());

        // 2. 로그인 → 응답 body에서 JWT를 꺼낸다
        String loginBody = mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"username":"integ","password":"1234"}
                        """))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        String accessToken = JsonPath.read(loginBody, "$.body.accessToken");

        // 3. Bearer 토큰을 실어 인증이 필요한 게시글 등록 — 필터가 실제로 검증한다
        mvc.perform(post("/boards")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"title":"통합테스트 글","content":"전 계층 통과"}
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.body.writer").value("integ"));

        // 4. 목록 조회 — 실제 DB(data.sql 3건 + 방금 등록 1건)에서 온다. 조회도 인증이 필요하다
        mvc.perform(get("/boards")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.body[0].title").value("통합테스트 글"));
    }

    @Test
    public void 토큰_없이_보호_API_401_test() throws Exception {
        // TODO 5: 토큰 없이 POST /boards 를 보내고 401과 $.status = 401 을 확인하세요
        // 슬라이스(@WebMvcTest)에서는 로드되지 않던 필터가 여기서는 실제로 요청을 끊는다
    }

    @Test
    public void 없는_글_조회_404_test() throws Exception {
        // TODO 6: GET /boards/99 에서 404와 $.status = 404 를 확인하세요
        // 전역 예외 처리(Resp 형식)까지 포함한 실패 경로 검증이다
    }
}
