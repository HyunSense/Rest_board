package board.repository.V2;

import board.entity.V2.Board;
import board.entity.V2.Member;
import board.repository.BoardRepository;
import board.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class BoardRepositoryV2Test {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    private static Member member;

    @BeforeEach
    void setUp() {

        member = Member.builder()
                .username("testUsername")
                .password("testPassword")
                .name("testName")
                .email("testEmail")
                .role("ROLE_USER")
                .build();

        memberRepository.save(member);
    }


    @Test
    @DisplayName("글 저장 및 조회")
    void findById() {
        //given
        Board board = Board.builder()
                .member(member)
                .title("testTitle")
                .content("testContent")
                .build();

        //when
        boardRepository.save(board);

        //then
        Board findBoard = boardRepository.findById(board.getId());
        assertThat(board).isEqualTo(findBoard);
        assertThat(findBoard.getMember()).isEqualTo(member);
        assertThat(findBoard.getTitle()).isEqualTo(board.getTitle());
        assertThat(findBoard.getContent()).isEqualTo(board.getContent());

    }

    @Test
    @DisplayName("저장되지 않은 글 조회")
    void findByNotExistsBoardId() {
        //given
        Long notExistsBoardId = 99999L;

        //when

        //then
        assertThatThrownBy(() -> boardRepository.findById(notExistsBoardId))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }


}