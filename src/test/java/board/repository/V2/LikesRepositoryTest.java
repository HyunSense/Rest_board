package board.repository.V2;

import board.entity.V2.Board;
import board.entity.V2.Likes;
import board.entity.V2.Member;
import board.repository.BoardRepository;
import board.repository.LikesRepository;
import board.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class LikesRepositoryTest {


    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private LikesRepository likesRepository;

    @Autowired
    private EntityManager em;

    private static Member member;

    private static Member likesMember;

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

        likesMember = Member.builder()
                .username("likesUsername")
                .password("likesPassword")
                .name("likesName")
                .email("likesEmail")
                .role("ROLE_USER")
                .build();

        board = Board.builder()
                .member(member)
                .title("testTitle")
                .content("testContent")
                .build();

        memberRepository.save(member);
        memberRepository.save(likesMember);
        boardRepository.save(board);

        System.out.println("=== SET UP ===");

    }

    @Test
    @DisplayName("좋아요 저장 및 조회")
    void findByMemberIdAndBoardId() {
        //given
        Likes likes = Likes.builder()
                .member(likesMember)
                .board(board)
                .build();

        //when
        likesRepository.save(likes);

        em.flush();
        em.clear();

        Likes findLikes = likesRepository.findByMemberIdAndBoardId(likesMember.getId(), board.getId()).orElseThrow();

        //then
        assertThat(findLikes.getMember().getId()).isEqualTo(likesMember.getId());
        assertThat(findLikes.getBoard().getId()).isEqualTo(board.getId());
    }

    @Test
    @DisplayName("좋아요 삭제")
    void deleteByMemberIdAndBoardId() {
        //given
        Likes likes = Likes.builder()
                .member(likesMember)
                .board(board)
                .build();

        //when
        likesRepository.save(likes);

        em.flush();
        em.clear();

        likesRepository.deleteByMemberIdAndBoardId(likesMember.getId(), board.getId());

        //then
        Optional<Likes> likesOpt =
                likesRepository.findByMemberIdAndBoardId(likesMember.getId(), board.getId());
        assertThat(likesOpt).isEmpty();
    }
}