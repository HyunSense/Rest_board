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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class BoardRepositoryTest {

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
        Board findBoard = boardRepository.findById(board.getId()).orElseThrow();
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
        Optional<Board> board = boardRepository.findById(notExistsBoardId);

        //then
        assertThat(board).isEmpty();
    }


    @Test
    @DisplayName("회원 아이디 포함 글 조회")
    void findWithUsernameById() {
        //given
        Board board = Board.builder()
                .member(member)
                .title("testTitle")
                .content("testContent")
                .build();

        boardRepository.save(board);

        //when
        Board findBoard = boardRepository.findWithUsernameById(board.getId()).orElseThrow();

        //then
        assertThat(board).isEqualTo(findBoard);
        assertThat(findBoard.getMember()).isEqualTo(member);
    }

    @Test
    @DisplayName("전체 글 조회")
    void findAllByMemberId() {
        //given
        for (int i = 0; i < 5; i++) {
            Board board = Board.builder()
                    .member(member)
                    .title("testTitle " + i)
                    .content("testContent " + i)
                    .build();

            boardRepository.save(board);
        }

        //when
        List<Board> boards = boardRepository.findAll();

        //then
        assertThat(boards).hasSize(5);
        boards.stream().map(Board::getTitle).forEach(System.out::println);
    }


    @Test
    @DisplayName("페이징 전체 글 조회")
    void findAllWithUsername() {
        //given
        for (int i = 0; i < 5; i++) {
            Board board = Board.builder()
                    .member(member)
                    .title("testTitle " + i)
                    .content("testContent " + i)
                    .build();

            boardRepository.save(board);
        }

        //when
        int limit = 3;
        int offset = 2;
        // expect: {2,3,4}
        List<Board> boards = boardRepository.findAllWithUsername(limit, offset);

        //then
        assertThat(boards).hasSize(limit);
        boards.stream().map(Board::getTitle).forEach(System.out::println);
    }

    @Test
    @DisplayName("타입 + 키워드 글 조회")
    void findBoardByTypeAndKeyword() {
        //given
        Board board = Board.builder()
                .member(member)
                .title("testTitle")
                .content("testContent")
                .build();
        boardRepository.save(board);

        //when
//        String type = "username";
//        String keyword = "Us";

        String type = "title";
        String keyword = "Ti";


        List<Board> boards = boardRepository.findBoardByTypeAndKeyword(type, keyword);

        //then
        assertThat(boards).hasSize(1);
    }

    @Test
    @DisplayName("글 수정")
    void update() {
        //given
        Board board = Board.builder()
                .member(member)
                .title("testTitle")
                .content("testContent")
                .build();

        boardRepository.save(board);

        //when
        String updateTitle = "updateTitle";
        String updateContent = "updateContent";

        board.update(updateTitle, updateContent);

        //then
        Board findBoard = boardRepository.findById(board.getId()).orElseThrow();
        assertThat(findBoard.getTitle()).isEqualTo(updateTitle);
        assertThat(findBoard.getContent()).isEqualTo(updateContent);
    }

    @Test
    @DisplayName("글 삭제")
    void deleteById() {
        //given
        Board board = Board.builder()
                .member(member)
                .title("testTitle")
                .content("testContent")
                .build();

        boardRepository.save(board);

        //when
        board.delete();

        //then
        Optional<Board> findBoard = boardRepository.findById(board.getId());
        assertThat(findBoard).isEmpty();
    }
}