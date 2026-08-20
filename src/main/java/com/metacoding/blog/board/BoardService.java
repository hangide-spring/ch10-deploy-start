package com.metacoding.blog.board;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metacoding.blog.ex.Exception404;
import com.metacoding.blog.user.User;
import com.metacoding.blog.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public List<BoardResponse> findAll() {
        log.info("게시글 목록 조회"); // INFO — 주요 처리 흐름
        List<BoardResponse> result = boardRepository.findAll().stream().map(board -> BoardResponse.from(board))
                .toList();
        log.debug("조회 건수: {}", result.size()); // DEBUG — 기본 레벨(INFO)에서는 걸러진다
        return result;
    }

    public BoardResponse findById(Integer id) {
        return BoardResponse.from(getBoard(id));
    }

    @Transactional
    public BoardResponse save(BoardRequest request, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception404("사용자를 찾을 수 없습니다 : " + userId));
        Board board = request.toEntity(user); // 작성자는 토큰에서 온 사용자다
        boardRepository.save(board);
        return BoardResponse.from(board);
    }

    @Transactional
    public BoardResponse update(Integer id, BoardRequest request, Integer userId) {
        Board board = getBoard(id);
        board.checkOwner(userId); // 도메인 검증 — 판단은 엔티티가 한다
        board.update(request.title(), request.content());
        return BoardResponse.from(board);
    }

    @Transactional
    public void delete(Integer id, Integer userId) {
        Board board = getBoard(id);
        board.checkOwner(userId); // 도메인 검증 — 판단은 엔티티가 한다
        boardRepository.delete(board);
    }

    private Board getBoard(Integer id) {
        Board board = boardRepository.findById(id);
        if (board == null) {
            log.warn("존재하지 않는 게시글 조회 시도: {}", id); // WARN — 잘못됐지만 서버 오류는 아니다
            throw new Exception404("게시글을 찾을 수 없습니다 : " + id); // 던지기만 한다 — 잡는 것은 Advice의 몫
        }
        return board;
    }

}
