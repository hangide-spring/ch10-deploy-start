package com.metacoding.blog.board;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest; // 스프링 부트 4의 새 패키지 위치
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

// 웹 계층만 잘라 검증하는 슬라이스 테스트 — 6차시 테스트를 DTO 응답 계약 기준으로 수정했다
@WebMvcTest(BoardController.class)
public class BoardControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean // 컨트롤러가 의존하는 서비스를 가짜 객체로 대체한다
    private BoardService boardService;

    @Test
    public void list_test() throws Exception {
        // given — 가짜 서비스는 이제 엔티티가 아니라 응답 DTO(record)를 돌려준다
        BoardResponse response = new BoardResponse(1, "제목1", "내용1", "익명", null);
        given(boardService.findAll()).willReturn(List.of(response));

        // when & then
        mvc.perform(get("/boards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.body[0].title").value("제목1")); // 응답이 Resp로 감싸졌다
    }

    @Test
    public void save_test() throws Exception {
        // given — @WebMvcTest에는 필터가 없으므로 userId는 null로 들어온다(가짜 서비스라 무관)
        BoardResponse response = new BoardResponse(4, "새글제목", "새글내용", "ssar", null);
        given(boardService.save(any(BoardRequest.class), any())).willReturn(response);

        // when & then
        mvc.perform(post("/boards")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"새글제목\",\"content\":\"새글내용\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.body.id").value(4));
    }

    @Test
    public void save_valid_fail_test() throws Exception {
        // when & then — 제목이 빈 값이면 @Valid가 400으로 거절한다 (서비스까지 가지도 않는다)
        mvc.perform(post("/boards")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"\",\"content\":\"내용\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400)); // 검증 실패도 이제 Resp 형식이다
    }
}
