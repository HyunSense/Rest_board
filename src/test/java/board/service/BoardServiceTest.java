package board.service;

import board.dto.request.board.*;
import board.dto.response.board.*;
import board.entity.Board;
import board.entity.Comment;
import board.entity.Likes;
import board.mapper.BoardMapper;
import board.mapper.resultset.BoardResultSet;
import board.mapper.resultset.CommentResultSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardMapper boardMapper;
    @Spy
    private Board board;
    @Mock
    private Comment comment;
    @Mock
    private Likes likes;
    @Mock
    private BoardResultSet mockBoardResultSet;
    @Mock
    private List<BoardResultSet> MockBoardResultSetList;
    @InjectMocks
    private BoardServiceImpl boardService;

    private Long boardId;
    private Long memberId;
    private Long commentId;
    private PostBoardRequestDto postBoardRequestDto;
    private GetBoardAllRequestDto getBoardAllRequestDto;
    private PatchBoardRequestDto patchBoardRequestDto;
    private PostCommentRequestDto postCommentRequestDto;
    private GetSearchBoardListRequestDto getSearchBoardListRequestDto;


    @BeforeEach
    void setUp() {
        postBoardRequestDto = new PostBoardRequestDto();
        postBoardRequestDto.setTitle("create Title");
        postBoardRequestDto.setContent("create Title");

        patchBoardRequestDto = new PatchBoardRequestDto();
        patchBoardRequestDto.setTitle("patch Title");
        patchBoardRequestDto.setContent("patch Content");

        getBoardAllRequestDto = new GetBoardAllRequestDto();
        getBoardAllRequestDto.setPage(1);
        getBoardAllRequestDto.setLimit(10);

        postCommentRequestDto = new PostCommentRequestDto();
        postCommentRequestDto.setContent("post comment");

        getSearchBoardListRequestDto = new GetSearchBoardListRequestDto();

//        Board InitBoard = Board.builder()
//                .id(1L)
//                .memberId(1L)
//                .title("setup title")
//                .content("setup content")
//                .viewCount(0)
//                .commentCount(0)
//                .likesCount(0)
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .isDeleted(0).build();

        boardId = 1L;
        memberId = 1L;
        commentId = 1L;

    }

    @Test
    @DisplayName("게시글 작성 성공")
    void createdBoardSuccess() {
        //given
        willDoNothing().given(boardMapper).saveBoard(any(Board.class));

        //when
        Long memberId = 1L;
        ResponseEntity<? super PostBoardResponseDto> response = boardService.createBoard(memberId, postBoardRequestDto);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(boardMapper).saveBoard(any(Board.class));
    }

    @Test
    @DisplayName("게시글 작성 데이터베이스 에러")
    void createBoardDatabaseError() {
        //given
        doThrow(new RuntimeException()).when(boardMapper).saveBoard(any(Board.class));

        //when
        Long memberId = 1L;
        ResponseEntity<? super PostBoardResponseDto> response = boardService.createBoard(memberId, postBoardRequestDto);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        verify(boardMapper).saveBoard(any(Board.class));
    }

    @Test
    @DisplayName("게시글 단일 조회 성공")
    void getBoardByIdSuccess() {
        //given
        given(boardMapper.getBoardById(boardId)).willReturn(mockBoardResultSet);
        given(boardMapper.findBoardById(boardId)).willReturn(board);
        willDoNothing().given(board).increaseViewCount();
        willDoNothing().given(boardMapper).updateViewCountBoard(board);


        //when
        ResponseEntity<? super GetBoardResponseDto> response = boardService.getBoardById(boardId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(boardMapper).getBoardById(boardId);
        verify(boardMapper).findBoardById(boardId);
        verify(board).increaseViewCount();
        verify(boardMapper).updateViewCountBoard(board);
    }
    
    @Test
    @DisplayName("없는 단일 게시글 조회")
    void getBoardByIdNotExistBoard() {
        //given
        given(boardMapper.getBoardById(boardId)).willReturn(null);

        //when
        ResponseEntity<? super GetBoardResponseDto> response = boardService.getBoardById(boardId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(boardMapper).getBoardById(boardId);
    }

    @Test
    @DisplayName("게시글 단일 조회 데이터베이스 에러")
    void getBoardByIdDatabaseError() {
        //given
        doThrow(new RuntimeException()).when(boardMapper).getBoardById(boardId);

        //when
        ResponseEntity<? super GetBoardResponseDto> response = boardService.getBoardById(boardId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("게시글 수정 성공")
    void updateBoardSuccess() {
        //given
        given(boardMapper.findBoardById(boardId)).willReturn(board);
        given(board.getMemberId()).willReturn(memberId);
        willDoNothing().given(board).patchBoard(patchBoardRequestDto);
        willDoNothing().given(boardMapper).updateBoard(board);

        //when
        ResponseEntity<? super PatchBoardResponseDto> response = boardService.updateBoard(patchBoardRequestDto, memberId, boardId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(boardMapper).findBoardById(boardId);
        verify(board).patchBoard(patchBoardRequestDto);
        verify(boardMapper).updateBoard(board);
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정 시도")
    void updateBoardNotExistBoard() {
        //given
        given(boardMapper.findBoardById(boardId)).willReturn(null);

        //when
        ResponseEntity<? super PatchBoardResponseDto> response = boardService.updateBoard(patchBoardRequestDto, memberId, boardId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(boardMapper).findBoardById(boardId);

    }

    @Test
    @DisplayName("게시글 수정 권한 없음")
    void updateBoardNotPermission() {
        //given
        Long differentMemberId = 2L;
        given(boardMapper.findBoardById(boardId)).willReturn(board);
        given(board.getMemberId()).willReturn(differentMemberId);

        //when
        ResponseEntity<? super PatchBoardResponseDto> response = boardService.updateBoard(patchBoardRequestDto, memberId, boardId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        verify(boardMapper).findBoardById(boardId);
    }

    @Test
    @DisplayName("제목, 내용 빈 문자열로 수정 시도")
    void updateBoardValidationFailedBlankDto() {
        //given
        PatchBoardRequestDto blankDto = new PatchBoardRequestDto();
        blankDto.setTitle("");
        blankDto.setContent("");
        given(boardMapper.findBoardById(boardId)).willReturn(board);
        given(board.getMemberId()).willReturn(memberId);

        //when
        ResponseEntity<? super PatchBoardResponseDto> response = boardService.updateBoard(blankDto, memberId, boardId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(boardMapper).findBoardById(boardId);
    }

    @Test
    @DisplayName("제목, 내용 NULL로 수정 시도")
    void updateBoardValidationFailedNullDto() {
        //given
        PatchBoardRequestDto nullDto = new PatchBoardRequestDto();
        nullDto.setTitle(null);
        nullDto.setContent(null);
        given(boardMapper.findBoardById(boardId)).willReturn(board);
        given(board.getMemberId()).willReturn(memberId);

        //when
        ResponseEntity<? super PatchBoardResponseDto> response = boardService.updateBoard(nullDto, memberId, boardId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(boardMapper).findBoardById(boardId);
    }

    @Test
    @DisplayName("게시글 수정 데이터베이스 에러")
    void updateBoardDatabaseError() {
        //given
        given(boardMapper.findBoardById(boardId)).willReturn(board);
        given(board.getMemberId()).willReturn(memberId);
        willDoNothing().given(board).patchBoard(patchBoardRequestDto);
        doThrow(new RuntimeException()).when(boardMapper).updateBoard(board);

        //when
        ResponseEntity<? super PatchBoardResponseDto> response = boardService.updateBoard(patchBoardRequestDto, memberId, boardId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        verify(boardMapper).updateBoard(board);
    }

    @Test
    @DisplayName("댓글 0개 이상 일때 게시글 삭제 성공")
    void deleteBoardSuccess() {
        //given
        int commentCount = 2;
        given(boardMapper.findBoardById(boardId)).willReturn(board);
        given(board.getMemberId()).willReturn(memberId);
        given(boardMapper.countCommentByBoardId(boardId)).willReturn(commentCount);
        willDoNothing().given(boardMapper).deleteCommentBoardAllByBoardId(boardId);
        willDoNothing().given(boardMapper).deleteBoardById(boardId);

        //when
        ResponseEntity<? super DeleteBoardResponseDto> response = boardService.deleteBoard(memberId, boardId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(boardMapper).findBoardById(boardId);
        verify(boardMapper).countCommentByBoardId(boardId);
        verify(boardMapper).deleteCommentBoardAllByBoardId(boardId);
        verify(boardMapper).deleteBoardById(boardId);
    }

    @Test
    @DisplayName("댓글이 0개 일때 게시글 삭제 성공")
    void deleteBoardCommentCountZeroSuccess() {
        //given
        int zeroCount = 0;
        given(boardMapper.findBoardById(boardId)).willReturn(board);
        given(board.getMemberId()).willReturn(memberId);
        given(boardMapper.countCommentByBoardId(boardId)).willReturn(zeroCount);
        willDoNothing().given(boardMapper).deleteBoardById(boardId);

        //when
        ResponseEntity<? super DeleteBoardResponseDto> response = boardService.deleteBoard(memberId, boardId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(boardMapper).findBoardById(boardId);
        verify(boardMapper).countCommentByBoardId(boardId);
        verify(boardMapper).deleteBoardById(boardId);
    }

    @Test
    @DisplayName("게시글 존재하지 않을때 삭제 시도")
    void deleteBoardNotExistBoard() {
        //given
        given(boardMapper.findBoardById(boardId)).willReturn(null);

        //when
        ResponseEntity<? super DeleteBoardResponseDto> response = boardService.deleteBoard(memberId, boardId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(boardMapper).findBoardById(boardId);
    }

    @Test
    @DisplayName("게시글 삭제 권한 없음")
    void deleteBoardNotPermission() {
        //given
        Long differentMemberId = 2L;
        given(boardMapper.findBoardById(boardId)).willReturn(board);
        given(board.getMemberId()).willReturn(differentMemberId);

        //when
        ResponseEntity<? super DeleteBoardResponseDto> response = boardService.deleteBoard(memberId, boardId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        verify(boardMapper).findBoardById(boardId);
    }

    @Test
    @DisplayName("게시글 삭제 데이터베이스 에러")
    void deleteBoardDatabaseError() {
        //given
        doThrow(new RuntimeException()).when(boardMapper).findBoardById(boardId);

        //when
        ResponseEntity<? super DeleteBoardResponseDto> response = boardService.deleteBoard(memberId, boardId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("게시글 전체조회 성공")
    void getAllBoardsSuccess() {

        //given
        List<BoardResultSet> boards = new ArrayList<>();
        int offset = (getBoardAllRequestDto.getPage() - 1) * getBoardAllRequestDto.getLimit();
        given(boardMapper.getAllBoard(getBoardAllRequestDto.getLimit(), offset)).willReturn(boards);

        //when
        ResponseEntity<? super GetBoardAllResponseDto> response = boardService.getAllBoards(getBoardAllRequestDto);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(boardMapper).getAllBoard(getBoardAllRequestDto.getLimit(), offset);
    }

    @Test
    @DisplayName("게시글 전체조회시 존재하지않는 게시글")
    void getAllBoardsNotExistBoard() {
        //given
        int offset = (getBoardAllRequestDto.getPage() - 1) * getBoardAllRequestDto.getLimit();
        given(boardMapper.getAllBoard(getBoardAllRequestDto.getLimit(), offset)).willReturn(null);

        //when
        ResponseEntity<? super GetBoardAllResponseDto> response = boardService.getAllBoards(getBoardAllRequestDto);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(boardMapper).getAllBoard(getBoardAllRequestDto.getLimit(), offset);
    }

    @Test
    @DisplayName("게시글 전체조회시 데이터베이스 에러")
    void getAllBoardsDatabaseError() {
        //given
        int offset = (getBoardAllRequestDto.getPage() - 1) * getBoardAllRequestDto.getLimit();
        doThrow(new RuntimeException()).when(boardMapper).getAllBoard(getBoardAllRequestDto.getLimit(), offset);

        //when
        ResponseEntity<? super GetBoardAllResponseDto> response = boardService.getAllBoards(getBoardAllRequestDto);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("댓글 작성 성공")
    void createCommentSuccess() {
        //given
        given(boardMapper.findBoardById(boardId)).willReturn(board);
        willDoNothing().given(board).increaseComment();
        willDoNothing().given(boardMapper).updateCommentCountBoard(board);
        willDoNothing().given(boardMapper).saveCommentBoard(any(Comment.class));

        //when
        ResponseEntity<? super PostCommentResponseDto> response =
                boardService.createComment(postCommentRequestDto, memberId, boardId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(boardMapper).findBoardById(boardId);
        verify(board).increaseComment();
        verify(boardMapper).updateCommentCountBoard(board);
        verify(boardMapper).saveCommentBoard(any(Comment.class));
    }

    @Test
    @DisplayName("게시글이 존재하지 않을때 댓글 작성 시도")
    void createCommentNotExistBoard() {
        //given
        given(boardMapper.findBoardById(boardId)).willReturn(null);

        //when
        ResponseEntity<? super PostCommentResponseDto> response =
                boardService.createComment(postCommentRequestDto, memberId, boardId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(boardMapper).findBoardById(boardId);
    }

    @Test
    @DisplayName("댓글 작성시 데이터베이스 에러")
    void createCommentDatabaseError() {
        //given
        doThrow(new RuntimeException()).when(boardMapper).findBoardById(boardId);

        //when
        ResponseEntity<? super PostCommentResponseDto> response =
                boardService.createComment(postCommentRequestDto, memberId, boardId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void deleteCommentSuccess() {
        //given
        given(boardMapper.findBoardById(boardId)).willReturn(board);
        given(boardMapper.findCommentById(commentId)).willReturn(comment);
        given(comment.getMemberId()).willReturn(memberId);
        willDoNothing().given(boardMapper).deleteCommentBoardById(boardId, commentId);
        willDoNothing().given(board).decreaseComment();
        willDoNothing().given(boardMapper).updateCommentCountBoard(board);

        //when
        ResponseEntity<? super DeleteCommentResponseDto> response = boardService.deleteComment(memberId, boardId, commentId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(boardMapper).findBoardById(boardId);
        verify(boardMapper).findCommentById(commentId);
        verify(comment).getMemberId();
        verify(boardMapper).deleteCommentBoardById(boardId, commentId);
        verify(board).decreaseComment();
        verify(boardMapper).updateCommentCountBoard(board);
    }

    @Test
    @DisplayName("존재하지 않는 게시판 댓글 삭제 시도")
    void deleteCommentNotExistBoard() {
        //given
        given(boardMapper.findBoardById(boardId)).willReturn(null);

        //when
        ResponseEntity<? super DeleteCommentResponseDto> response = boardService.deleteComment(memberId, boardId, commentId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(boardMapper).findBoardById(boardId);
    }

    @Test
    @DisplayName("존재하지 않는 댓글 삭제 시도")
    void deleteCommentNotExistComment() {
        //given

        given(boardMapper.findBoardById(boardId)).willReturn(board);
        given(boardMapper.findCommentById(commentId)).willReturn(null);

        //when
        ResponseEntity<? super DeleteCommentResponseDto> response = boardService.deleteComment(memberId, boardId, commentId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(boardMapper).findBoardById(boardId);
        verify(boardMapper).findCommentById(commentId);
    }

    @Test
    @DisplayName("댓글 삭제 권한 없음")
    void deleteCommentNotPermission() {
        //given
        Long differentMemberId = 2L;
        given(boardMapper.findBoardById(boardId)).willReturn(board);
        given(boardMapper.findCommentById(commentId)).willReturn(comment);
        given(comment.getMemberId()).willReturn(differentMemberId);

        //when
        ResponseEntity<? super DeleteCommentResponseDto> response = boardService.deleteComment(memberId, boardId, commentId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("댓글 삭제 데이터베이스 에러")
    void deleteCommentDatabaseError() {
        //given
        given(boardMapper.findBoardById(boardId)).willReturn(board);
        given(boardMapper.findCommentById(commentId)).willReturn(comment);
        given(comment.getMemberId()).willReturn(memberId);
        doThrow(new RuntimeException()).when(boardMapper).deleteCommentBoardById(boardId, commentId);


        //when
        ResponseEntity<? super DeleteCommentResponseDto> response = boardService.deleteComment(memberId, boardId, commentId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        verify(boardMapper).findBoardById(boardId);
        verify(boardMapper).findCommentById(commentId);
        verify(comment).getMemberId();
    }

    @Test
    @DisplayName("검색한 게시글 조회 성공")
    void getSearchBoardSuccess() {
        //given
        BoardResultSet resultSet1 = BoardResultSet.builder()
                .id(1L)
                .title("First title")
                .content("First content")
                .author("Author")
                .viewCount(0)
                .commentCount(0)
                .likesCount(0)
                .createdAt("2024-07-27 02:16:33")
                .updatedAt("2024-07-27 02:16:33")
                .build();

        BoardResultSet resultSet2 = BoardResultSet.builder()
                .id(2L)
                .title("Second title")
                .content("Second content")
                .author("Author")
                .viewCount(0)
                .commentCount(0)
                .likesCount(0)
                .createdAt("2024-07-28 02:17:33")
                .updatedAt("2024-07-28 02:17:33")
                .build();

        String successType = "author";
        String expectedType = "username";
        String successKeyword = "Author";
        getSearchBoardListRequestDto.setType(successType);
        getSearchBoardListRequestDto.setKeyword(successKeyword);

        List<BoardResultSet> resultSetList = List.of(resultSet1, resultSet2);

        given(boardMapper.findBoardByTypeAndKeyword(expectedType, successKeyword)).willReturn(resultSetList);


        //when
        ResponseEntity<? super GetSearchBoardListResponseDto> response =
                boardService.getSearchBoard(getSearchBoardListRequestDto);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("검색한 게시글 0건 조회")
    void getSearchBoardSuccessNoCount() {
        //given
        String successType = "author";
        String expectedType = "username";
        String successKeyword = "NotExistResult";
        getSearchBoardListRequestDto.setType(successType);
        getSearchBoardListRequestDto.setKeyword(successKeyword);

        List<BoardResultSet> resultSetList = List.of(mockBoardResultSet);
        given(boardMapper.findBoardByTypeAndKeyword(expectedType, successKeyword)).willReturn(resultSetList);

        //when
        ResponseEntity<? super GetSearchBoardListResponseDto> response =
                boardService.getSearchBoard(getSearchBoardListRequestDto);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        GetSearchBoardListResponseDto responseBody = (GetSearchBoardListResponseDto) response.getBody();
        assertThat(responseBody).isNotNull();
        // 빈 배열조회
        assertThat(responseBody.getBoardList()).hasSize(1);
    }

    @Test
    @DisplayName("검색 게시글 조회시 데이터베이스 에러")
    void getSearchBoardDatabaseError() {
        //given
        String type = "author";
        String keyword = "keyword";

        getSearchBoardListRequestDto.setType(type);
        getSearchBoardListRequestDto.setKeyword(keyword);
        doThrow(new RuntimeException()).when(boardMapper).findBoardByTypeAndKeyword(type, keyword);

        //when
        ResponseEntity<? super GetSearchBoardListResponseDto> response =
                boardService.getSearchBoard(getSearchBoardListRequestDto);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("댓글 조회 성공")
    void getCommentListSuccess() {
        //given
        CommentResultSet commentResultSet = CommentResultSet.builder()
                .id(1L)
                .username("username")
                .content("content")
                .createdAt("2023-12-12 13:01:33")
                .build();

        List<CommentResultSet> commentResultSetList = List.of(commentResultSet);
        given(boardMapper.findBoardById(boardId)).willReturn(board);
        given(boardMapper.findAllCommentByBoardId(boardId)).willReturn(commentResultSetList);

        //when
        ResponseEntity<? super GetCommentListResponseDto> response = boardService.getCommentList(boardId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(boardMapper).findBoardById(boardId);
        verify(boardMapper).findAllCommentByBoardId(boardId);
    }

    @Test
    @DisplayName("존재하지 않는 게시글에서 댓글 조회 시도")
    void getCommentListNotExistBoard() {
        //given
        given(boardMapper.findBoardById(boardId)).willReturn(null);

        //when
        ResponseEntity<? super GetCommentListResponseDto> response = boardService.getCommentList(boardId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(boardMapper).findBoardById(boardId);
    }

    @Test
    @DisplayName("댓글 조회시 데이터베이스 에러")
    void getCommentListDatabaseError() {
        //given
        given(boardMapper.findBoardById(boardId)).willReturn(board);
        doThrow(new RuntimeException()).when(boardMapper).findAllCommentByBoardId(boardId);

        //when
        ResponseEntity<? super GetCommentListResponseDto> response = boardService.getCommentList(boardId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        verify(boardMapper).findBoardById(boardId);
        verify(boardMapper).findAllCommentByBoardId(boardId);
    }

    @Test
    @DisplayName("좋아요 존재할때 삭제 성공")
    void toggleLikesSuccessDelete() {
        //given
        given(boardMapper.findBoardById(boardId)).willReturn(board);
        given(boardMapper.findLikesByMemberIdAndBoardId(memberId, boardId)).willReturn(likes);
        willDoNothing().given(boardMapper).deleteLikesBoard(likes);
        willDoNothing().given(board).decreaseLikes();
        willDoNothing().given(boardMapper).updateLikesCountBoard(board);

        //when
        ResponseEntity<? super GetLikesResponseDto> response =
                boardService.toggleLikes(memberId, boardId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(boardMapper).findBoardById(boardId);
        verify(boardMapper).findLikesByMemberIdAndBoardId(memberId, boardId);
        verify(boardMapper).deleteLikesBoard(likes);
        verify(board).decreaseLikes();
        verify(boardMapper).updateLikesCountBoard(board);
    }

    @Test
    @DisplayName("좋아요 존재하지 않을때 추가 성공")
    void toggleLikesSuccessSave() {
        //given
        given(boardMapper.findBoardById(boardId)).willReturn(board);
        given(boardMapper.findLikesByMemberIdAndBoardId(memberId, boardId)).willReturn(null);


        willDoNothing().given(boardMapper).saveLikesBoard(any(Likes.class));
        willDoNothing().given(board).increaseLikes();
        willDoNothing().given(boardMapper).updateLikesCountBoard(board);

        //when
        ResponseEntity<? super GetLikesResponseDto> response =
                boardService.toggleLikes(memberId, boardId);

        //then
        ArgumentCaptor<Likes> likesCaptor = ArgumentCaptor.forClass(Likes.class);
        verify(boardMapper).saveLikesBoard(likesCaptor.capture());
        Likes capturedLikes  = likesCaptor.getValue();
        assertThat(capturedLikes.getMemberId()).isEqualTo(memberId);
        assertThat(capturedLikes.getBoardId()).isEqualTo(boardId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(boardMapper).findBoardById(boardId);
        verify(boardMapper).findLikesByMemberIdAndBoardId(memberId, boardId);
        verify(boardMapper).saveLikesBoard(any(Likes.class));
        verify(board).increaseLikes();
        verify(boardMapper).updateLikesCountBoard(board);
    }

    @Test
    @DisplayName("게시글 존재하지 않을때 좋아요 시도")
    void toggleLikesNotExistsBoard() {
        //given
        given(boardMapper.findBoardById(boardId)).willReturn(null);

        //when
        ResponseEntity<? super GetLikesResponseDto> response =
                boardService.toggleLikes(memberId, boardId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(boardMapper).findBoardById(boardId);
    }

    @Test
    @DisplayName("좋아요 데이터베이스 에러")
    void toggleLikesDatabaseError() {
        //given
        given(boardMapper.findBoardById(boardId)).willReturn(board);
        doThrow(new RuntimeException()).when(boardMapper).findLikesByMemberIdAndBoardId(memberId, boardId);

        //when
        ResponseEntity<? super GetLikesResponseDto> response =
                boardService.toggleLikes(memberId, boardId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        verify(boardMapper).findBoardById(boardId);
    }
}
