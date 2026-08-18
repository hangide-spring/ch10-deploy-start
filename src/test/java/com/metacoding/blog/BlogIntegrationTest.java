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
        // TODO 1: 회원가입 — POST /join 에 {"username":"integ","password":"1234"} JSON을 보내고 201을 확인하세요
        //         힌트: mvc.perform(post("/join").contentType(MediaType.APPLICATION_JSON).content(...))
        //               .andExpect(status().isCreated());
        //         JSON 본문은 텍스트 블록(""")으로 쓰면 \" 이스케이프 없이 그대로 적을 수 있다:
        //         .content("""
        //                 {"username":"integ","password":"1234"}
        //                 """)

        // TODO 2: 로그인 — POST /login 후 응답 문자열을 andReturn().getResponse().getContentAsString()으로 받고,
        //         JsonPath.read(응답, "$.body.accessToken") 으로 JWT를 꺼내세요
        //         (로그인 응답의 body 안에 토큰이 있다 — 8차시에서 정한 계약)

        // TODO 3: 인증이 필요한 글 등록 — .header("Authorization", "Bearer " + 토큰)을 실어 POST /boards,
        //         201과 jsonPath("$.body.writer")가 "integ"인지 확인하세요 (필터가 실제로 검증한다)

        // TODO 4: 목록 조회 — GET /boards 에서 200, $.status = 200,
        //         $.body[0].title 이 방금 등록한 글인지 확인하세요 (실제 DB에서 온다)
    }

    @Test
    public void 토큰_없이_보호_API_401_test() throws Exception {
        // TODO 5: 토큰 없이 POST /boards 를 보내고 401과 $.status = 401 을 확인하세요
        //         슬라이스(@WebMvcTest)에서는 로드되지 않던 필터가 여기서는 실제로 요청을 끊는다
    }

    @Test
    public void 없는_글_조회_404_test() throws Exception {
        // TODO 6: GET /boards/99 에서 404와 $.status = 404 를 확인하세요
        //         전역 예외 처리(Resp 형식)까지 포함한 실패 경로 검증이다
    }
}
