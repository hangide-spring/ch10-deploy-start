package com.metacoding.blog.board;

import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // final 필드를 받는 생성자를 만들어 준다 — 생성자 주입
@Repository
public class BoardRepository {

    // 영속성 컨텍스트를 다루는 공식 창구 — 스프링이 만들어 둔 것을 주입받는다
    private final EntityManager em;

    public void save(Board board) {
        em.persist(board); // 영속화 — 이 순간부터 하이버네이트가 board를 관리한다
        System.out.println("영속화(persist) 완료 → id: " + board.getId());
    }

    public List<Board> findAll() {
        // JPQL — 테이블(board_tb)이 아니라 엔티티(Board)를 대상으로 쓰는 쿼리
        // left join fetch: 작성자(User)를 한 번의 쿼리로 함께 가져온다 — N+1 문제 해결
        // (left인 이유: 작성자 없는 옛 글도 목록에 남아야 하므로. 대안: hibernate.default_batch_fetch_size=100 → in 쿼리)
        List<Board> boardList = em
                .createQuery("select b from Board b left join fetch b.user order by b.id desc", Board.class)
                .getResultList();
        System.out.println("findAll 조회 건수: " + boardList.size());
        return boardList;
    }

    public Board findById(Integer id) {
        Board board = em.find(Board.class, id); // 1차 캐시를 먼저 보고, 없을 때만 SELECT가 나간다
        System.out.println("findById(" + id + ") → " + (board == null ? "없음" : board.getTitle()));
        return board;
    }

    public void delete(Board board) {
        em.remove(board);
        System.out.println("삭제(remove) 완료 → id: " + board.getId());
    }

    // 수정 메서드는 없다 — 트랜잭션 안에서 board.update(...)로 값을 바꾸면
    // 더티 체킹이 UPDATE를 만든다 (테스트에서 확인)
}
