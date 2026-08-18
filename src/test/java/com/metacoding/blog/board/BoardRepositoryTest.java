package com.metacoding.blog.board;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest; // 스프링 부트 4의 새 패키지 위치
import org.springframework.context.annotation.Import;

import jakarta.persistence.EntityManager;

// Repository만 잘라 검증하는 슬라이스 테스트 — 서버도 브라우저도 띄우지 않는다
// 직접 만든 @Repository 클래스는 자동으로 올라오지 않으므로 @Import로 명시한다
@Import(BoardRepository.class)
@DataJpaTest
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private EntityManager em; // flush 시점을 직접 보여주기 위해 테스트에서만 주입

    @Test
    public void save_test() {
        // given
        Board board = Board.builder().title("제목4").content("내용4").build();

        // when — id는 auto_increment(IDENTITY)라 DB가 만들어야 하므로
        // persist 시점에 쓰기 지연 없이 즉시 INSERT가 나간다 (콘솔 로그 확인)
        boardRepository.save(board);

        // then
        assertThat(board.getId()).isEqualTo(4);
        assertThat(board.getCreatedAt()).isNotNull();
    }

    @Test
    public void findAll_test() {
        // when — data.sql이 넣어 둔 초기 데이터 3건
        List<Board> boardList = boardRepository.findAll();

        // then — order by b.id desc 이므로 첫 번째가 제목3
        assertThat(boardList).hasSize(3);
        assertThat(boardList.get(0).getTitle()).isEqualTo("제목3");
    }

    @Test
    public void findById_test() {
        // when — 같은 id를 두 번 조회한다
        Board first = boardRepository.findById(1);
        Board second = boardRepository.findById(1);

        // then — SELECT 로그는 한 번만 나간다(1차 캐시), 두 결과는 같은 객체다
        assertThat(first).isSameAs(second);
        assertThat(first.getTitle()).isEqualTo("제목1");
    }

    @Test
    public void update_test() {
        // given
        Board board = boardRepository.findById(1);

        // when — 저장 메서드를 부르지 않고 값만 바꾼다
        board.update("수정된 제목", "수정된 내용");
        em.flush(); // 트랜잭션 커밋(flush) 시점에 더티 체킹이 UPDATE를 만든다 (콘솔 로그 확인)

        // then
        Board updated = boardRepository.findById(1);
        assertThat(updated.getTitle()).isEqualTo("수정된 제목");
    }

    @Test
    public void delete_test() {
        // given
        Board board = boardRepository.findById(1);

        // when
        boardRepository.delete(board);
        em.flush(); // DELETE가 나가는 시점을 로그로 확인

        // then
        assertThat(boardRepository.findAll()).hasSize(2);
    }
}
