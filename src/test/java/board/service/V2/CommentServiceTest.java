package board.service.V2;

import board.common.ResponseCode;
import board.dto.request.board.PostCommentRequestDto;
import board.dto.response.ResponseDto;
import board.entity.V2.Board;
import board.entity.V2.Comment;
import board.entity.V2.Member;
import board.exception.BoardNotFoundException;
import board.exception.CommentNotFoundException;
import board.exception.UnauthorizedUserException;
import board.repository.BoardRepository;
import board.repository.CommentRepository;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    private Board board;
    private Member member;
    private Comment comment;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private PostCommentRequestDto postCommentRequestDto;
    private Long boardId;
    private Long commentId;
    private Long memberId;

    @BeforeEach
    void setUp() {

        postCommentRequestDto = new PostCommentRequestDto();
        postCommentRequestDto.setContent("post comment");

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

        comment = spy(Comment.builder()
                .member(member)
                .board(board)
                .content("Test Comment")
                .createdAt(LocalDateTime.now())
                .build());

        memberId = 1L;
        boardId = 1L;
        commentId = 1L;
    }
    @Test
    @DisplayName("댓글 작성 성공")
    void createCommentSuccess() {

        //given
        given(memberRepository.findById(memberId)).willReturn(member);
        given(boardRepository.findById(boardId)).willReturn(Optional.of(board));
        willDoNothing().given(commentRepository).save(any(Comment.class));
        willDoNothing().given(board).increaseComment();

        //when

        ResponseDto response =
                commentService.createComment(postCommentRequestDto, memberId, boardId);

        //then
        assertThat(response.getCode()).isEqualTo(ResponseCode.SUCCESS.getCode());
        verify(boardRepository).findById(boardId);
        verify(commentRepository).save(any(Comment.class));
        verify(board).increaseComment();
    }

    @Test
    @DisplayName("게시글이 존재하지 않을때 댓글 작성 시도")
    void createCommentNotExistBoard() {
        //given
        given(boardRepository.findById(boardId)).willReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> commentService.createComment(postCommentRequestDto, memberId, boardId))
                .isInstanceOf(BoardNotFoundException.class)
                .hasMessage("존재하지 않는 게시글 입니다.");
        verify(boardRepository).findById(boardId);
    }


    @Test
    @DisplayName("댓글 삭제 성공")
    void deleteCommentSuccess() {
        //given
        given(boardRepository.findById(boardId)).willReturn(Optional.of(board));
        given(commentRepository.findWithUsernameById(commentId)).willReturn(Optional.of(comment));
        given(comment.getMember().getId()).willReturn(memberId);
        willDoNothing().given(board).decreaseComment();
        willDoNothing().given(comment).delete();

        //when
        ResponseDto response =
                commentService.deleteComment(memberId, boardId, commentId);

        //then
        assertThat(response.getCode()).isEqualTo(ResponseCode.SUCCESS.getCode());
        verify(boardRepository).findById(boardId);
        verify(commentRepository).findWithUsernameById(commentId);
        verify(board).decreaseComment();
        verify(comment).delete();
    }

    @Test
    @DisplayName("존재하지 않는 게시판 댓글 삭제 시도")
    void deleteCommentNotExistBoard() {
        //given
        given(boardRepository.findById(boardId)).willReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> commentService.deleteComment(memberId, boardId, commentId))
                .isInstanceOf(BoardNotFoundException.class)
                .hasMessage("존재하지 않는 게시글 입니다.");
        verify(boardRepository).findById(boardId);
    }

    @Test
    @DisplayName("존재하지 않는 댓글 삭제 시도")
    void deleteCommentNotExistComment() {
        //given

        given(boardRepository.findById(boardId)).willReturn(Optional.of(board));
        given(commentRepository.findWithUsernameById(commentId)).willReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> commentService.deleteComment(memberId, boardId, commentId))
                .isInstanceOf(CommentNotFoundException.class)
                .hasMessage("존재하지 않는 댓글 입니다.");
        verify(boardRepository).findById(boardId);
        verify(commentRepository).findWithUsernameById(commentId);
    }

    @Test
    @DisplayName("댓글 삭제 권한 없음")
    void deleteCommentNotPermission() {
        //given
        Long differentMemberId = 2L;
        given(boardRepository.findById(boardId)).willReturn(Optional.of(board));
        given(commentRepository.findWithUsernameById(commentId)).willReturn(Optional.of(comment));
        given(comment.getMember().getId()).willReturn(commentId);

        //when

        //then
        assertThatThrownBy(() -> commentService.deleteComment(differentMemberId, boardId, commentId))
                .isInstanceOf(UnauthorizedUserException.class)
                .hasMessage("삭제할 권한이 없습니다.");
        verify(boardRepository).findById(boardId);
        verify(commentRepository).findWithUsernameById(commentId);
    }
}
