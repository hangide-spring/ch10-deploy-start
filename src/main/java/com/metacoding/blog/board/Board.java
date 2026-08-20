package com.metacoding.blog.board;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import com.metacoding.blog.ex.Exception403;
import com.metacoding.blog.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 6차시에 역직렬화용으로 임시로 열었던 @Setter는 record DTO가 그 역할을 가져가면서 삭제됐다 — 엔티티가 다시 닫혔다
@NoArgsConstructor // JPA(하이버네이트)가 리플렉션으로 객체를 만들 때 기본 생성자가 필요하다
@Getter
@Table(name = "board_tb") // 이 클래스가 매핑될 테이블 이름
@Entity // 하이버네이트가 이 클래스를 보고 테이블을 만든다 (ddl-auto=create)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id는 DB의 auto_increment가 만든다
    private Integer id;
    @Column(length = 20) // varchar(20) — 제목 컬럼 폭
    private String title;
    @Column(length = 100) // varchar(100) — 내용 컬럼 폭
    private String content;

    @CreationTimestamp // insert 시점에 Hibernate가 시간을 자동으로 넣는다
    private Timestamp createdAt;

    // 게시글 여러 개가 사용자 한 명에 속한다(N:1)
    // 객체는 User를 통째로 참조하지만, 테이블에는 user_id 외래키 컬럼 하나가 생긴다
    @ManyToOne
    private User user;

    @Builder
    public Board(Integer id, String title, String content, Timestamp createdAt, User user) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.user = user;
    }

    // 세터 대신 의미 있는 변경 메서드 — 트랜잭션 안에서 이 값이 바뀌면
    // 하이버네이트의 더티 체킹이 UPDATE 쿼리를 대신 만들어 준다
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 도메인 검증 — "이 글의 주인이 맞는가"는 데이터를 가진 엔티티가 스스로 지킨다
    // 서비스는 묻기만 하고, 판단 기준은 Board 안에 산다 (9차시: 서비스의 checkOwner를 도메인으로 이동)
    public void checkOwner(Integer userId) {
        if (!user.getId().equals(userId)) {
            throw new Exception403("작성자가 아닙니다");
        }
    }
}
