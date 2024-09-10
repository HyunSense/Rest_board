package board.repository.V2;

import board.entity.V2.Board;
import board.entity.V2.Comment;
import board.entity.V2.Member;
import board.repository.BoardRepository;
import board.repository.CommentRepository;
import board.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
class CommentRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private CommentRepository commentRepository;

    private static Member member;
    private static Member commentMember;
    private static Board board;

    @BeforeEach
    void setUp() {

        member = Member.builder()
                .username("testUsername")
                .password("testPassword")
                .name("testName")
                .email("testEmail")
                .role("ROLE_USER")
                .build();

        commentMember = Member.builder()
                .username("commentUsername")
                .password("commentPassword")
                .name("commentName")
                .email("commentEmail")
                .role("ROLE_USER")
                .build();

        board = Board.builder()
                .member(member)
                .title("testTitle")
                .content("testContent")
                .build();

        memberRepository.save(member);
        memberRepository.save(commentMember);
        boardRepository.save(board);

        System.out.println("=== SET UP ===");
    }

    @Test
    @DisplayName("댓글 저장 및 조회")
    void findById() {
        //given
        Comment comment = Comment.builder()
                .member(commentMember)
                .board(board)
                .content("testContent")
                .build();

        //when
        commentRepository.save(comment);

        Comment findComment = commentRepository.findById(comment.getId()).orElseThrow();

        //then
        assertThat(findComment).isEqualTo(comment);
        assertThat(findComment.getMember()).isEqualTo(commentMember);
        assertThat(findComment.getBoard()).isEqualTo(board);
    }

    @Test
    @DisplayName("회원 이름 포함 댓글 조회")
    void findWithUsernameById() {
        //given
        Comment comment = Comment.builder()
                .member(commentMember)
                .board(board)
                .content("testContent")
                .build();

        //when
        commentRepository.save(comment);

        em.flush();
        em.clear();

        Comment findComment
                = commentRepository.findWithUsernameById(comment.getId()).orElseThrow();

        //then
//        assertThat(findComment).isEqualTo(comment);
//        assertThat(findComment.getMember()).isEqualTo(commentMember);
//        assertThat(findComment.getBoard()).isEqualTo(board);
        assertThat(findComment.getMember().getUsername()).isEqualTo(commentMember.getUsername());
        assertThat(findComment.getBoard().getTitle()).isEqualTo(comment.getBoard().getTitle());
    }

    @Test
    @DisplayName("댓글 전체 조회")
    void findAllByBoardId() {
        //given
        for (int i = 0; i < 5; i++) {
            Comment comment = Comment.builder()
                    .member(commentMember)
                    .board(board)
                    .content("testContent " + i)
                    .build();

            commentRepository.save(comment);
        }

        em.flush();
        em.clear();

        //when
        List<Comment> comments = commentRepository.findAllByBoardId(board.getId());

        //then
        System.out.println(comments.get(0).getBoard().getTitle());
        System.out.println(comments.get(0).getMember().getUsername());
        assertThat(comments).hasSize(5);
        comments.forEach(c -> System.out.println(c.getContent()));
    }

    @Test
    @DisplayName("회원이름 포함 댓글 전체 조회")
    void findAllWithUsernameByBoardId() {
        //given
        for (int i = 0; i < 5; i++) {
            Comment comment = Comment.builder()
                    .member(commentMember)
                    .board(board)
                    .content("testContent " + i)
                    .build();

            commentRepository.save(comment);
        }

        em.flush();
        em.clear();

        //when
        List<Comment> comments = commentRepository.findAllWithUsernameByBoardId(board.getId());

        //then
        System.out.println(comments.get(0).getBoard().getTitle());
        System.out.println(comments.get(0).getMember().getUsername());
        assertThat(comments).hasSize(5);
        comments.forEach(c -> System.out.println(c.getContent()));
    }

    @Test
    @DisplayName("댓글 삭제")
    void deleteById() {
        //given
        Comment comment = Comment.builder()
                .member(commentMember)
                .board(board)
                .content("testContent")
                .build();
        commentRepository.save(comment);

        //when
        comment.delete();

        //then
        Optional<Comment> commentOpt = commentRepository.findById(comment.getId());
        assertThat(commentOpt).isEmpty();
    }

    @Test
    @DisplayName("게시글의 댓글 전체 삭제")
    void deleteAllByBoardId() {
        //given
        for (int i = 0; i < 5; i++) {
            Comment comment = Comment.builder()
                    .member(commentMember)
                    .board(board)
                    .content("testContent " + i)
                    .build();

            commentRepository.save(comment);
        }

        //when
        commentRepository.deleteAllByBoardId(board.getId());

        //then
        List<Comment> comments = commentRepository.findAllByBoardId(board.getId());
        assertThat(comments).isEmpty();

    }

    @Test
    @DisplayName("게시글의 댓글 개수 조회")
    void countByBoardId() {
        //given
        for (int i = 0; i < 5; i++) {
            Comment comment = Comment.builder()
                    .member(commentMember)
                    .board(board)
                    .content("testContent " + i)
                    .build();

            commentRepository.save(comment);
        }

        //when
        int count = commentRepository.countByBoardId(board.getId());

        //then
        assertThat(count).isEqualTo(5);
    }
}
