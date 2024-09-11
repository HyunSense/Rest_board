package board.service.V2;

import board.common.ResponseCode;
import board.dto.response.ResponseDto;
import board.entity.V2.Board;
import board.entity.V2.Likes;
import board.entity.V2.Member;
import board.exception.BoardNotFoundException;
import board.repository.BoardRepository;
import board.repository.CommentRepository;
import board.repository.LikesRepository;
import board.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LikesServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private LikesRepository likesRepository;

    @InjectMocks
    private LikesServiceImpl likesService;

    @Mock
    private Likes likes;

    private Board board;
    private Member member;
    private Long boardId;
    private Long memberId;

    @BeforeEach
    void setUp() {

        member = spy(Member.builder()
                .username("testUsername")
                .password("testPassword")
                .name("testName")
                .email("testEmail")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .role("ROLE_USER")
                .build());


        board = spy(Board.builder()
                .member(member)
                .title("Test Title")
                .content("Test Content")
                .viewCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());

        memberId = 1L;
        boardId = 1L;
    }

    @Test
    @DisplayName("좋아요 존재할때 삭제 성공")
    void toggleLikesSuccessDelete() {
        //given
        given(memberRepository.findById(memberId)).willReturn(member);
        given(boardRepository.findById(boardId)).willReturn(Optional.of(board));

        given(likesRepository.findByMemberIdAndBoardId(memberId, boardId)).willReturn(Optional.of(likes));

        willDoNothing().given(likesRepository).deleteByMemberIdAndBoardId(memberId, boardId);
        willDoNothing().given(board).decreaseLikes();

        //when
        ResponseDto response = likesService.toggleLikes(memberId, boardId);

        //then
        assertThat(response.getCode()).isEqualTo(ResponseCode.SUCCESS.getCode());
        verify(memberRepository).findById(memberId);
        verify(boardRepository).findById(boardId);
        verify(likesRepository).findByMemberIdAndBoardId(memberId, boardId);
        verify(board).decreaseLikes();
    }

    @Test
    @DisplayName("좋아요 존재하지 않을때 추가 성공")
    void toggleLikesSuccessSave() {
        //given
        given(memberRepository.findById(memberId)).willReturn(member);
        given(boardRepository.findById(boardId)).willReturn(Optional.of(board));
        given(likesRepository.findByMemberIdAndBoardId(memberId, boardId)).willReturn(Optional.empty());

        willDoNothing().given(likesRepository).save(any(Likes.class));
        willDoNothing().given(board).increaseLikes();

        //when
        ResponseDto response = likesService.toggleLikes(memberId, boardId);

        //then
        assertThat(response.getCode()).isEqualTo(ResponseCode.SUCCESS.getCode());
        verify(memberRepository).findById(memberId);
        verify(boardRepository).findById(boardId);
        verify(likesRepository).findByMemberIdAndBoardId(memberId, boardId);
        verify(likesRepository).save(any(Likes.class));
        verify(board).increaseLikes();

    }

    @Test
    @DisplayName("게시글 존재하지 않을때 좋아요 시도")
    void toggleLikesNotExistsBoard() {
        //given
        given(memberRepository.findById(memberId)).willReturn(member);
        given(boardRepository.findById(boardId)).willReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> likesService.toggleLikes(memberId, boardId))
                .isInstanceOf(BoardNotFoundException.class)
                .hasMessage("존재하지 않는 게시글 입니다.");
        verify(memberRepository).findById(memberId);
        verify(boardRepository).findById(boardId);
    }
}
