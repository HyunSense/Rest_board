package board.service.V2;

import board.common.ResponseCode2;
import board.dto.request.board.GetBoardAllRequestDto;
import board.dto.request.board.GetSearchBoardListRequestDto;
import board.dto.request.board.PatchBoardRequestDto;
import board.dto.request.board.PostBoardRequestDto;
import board.dto.response.DataResponseDto;
import board.dto.response.ResponseDto;
import board.entity.V2.Board;
import board.entity.V2.Member;
import board.exception.BoardNotFoundException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private BoardServiceImpl boardService;

    private Board board;
    private Member member;

    private Long boardId;
    private Long memberId;

    private PostBoardRequestDto postBoardRequestDto;
    private GetBoardAllRequestDto getBoardAllRequestDto;
    private PatchBoardRequestDto patchBoardRequestDto;
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

        getSearchBoardListRequestDto = new GetSearchBoardListRequestDto();

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
    @DisplayName("게시글 작성 성공")
    void createdBoardSuccess() {
        //given
        given(memberRepository.findById(memberId)).willReturn(member);
        willDoNothing().given(boardRepository).save(any(Board.class));

        //when
        ResponseDto response = boardService.createBoard(memberId, postBoardRequestDto);

        //then
        assertThat(response.getCode()).isEqualTo(ResponseCode2.SUCCESS.getValue());
        assertThat(response.getMessage()).isEqualTo(ResponseCode2.SUCCESS.getDescription());
        verify(boardRepository).save(any(Board.class));
    }

    @Test
    @DisplayName("게시글 단일 조회 성공")
    void findWithUsernameByIdSuccess() {

        given(boardRepository.findWithUsernameById(boardId)).willReturn(Optional.of(board));
        willDoNothing().given(board).increaseViewCount();

        //when
        ResponseDto response = boardService.getBoardById(boardId);

        //then
        assertThat(response.getCode()).isEqualTo(ResponseCode2.SUCCESS.getValue());
        verify(boardRepository).findWithUsernameById(boardId);
        verify(board).increaseViewCount();
    }

    @Test
    @DisplayName("없는 단일 게시글 조회")
    void findWithUsernameByIdNotExistBoard() {
        //given
        Long notExistBoardId = 99999L;
        given(boardRepository.findWithUsernameById(notExistBoardId)).willReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> boardService.getBoardById(notExistBoardId))
                .isInstanceOf(BoardNotFoundException.class)
                .hasMessage("존재하지 않는 게시글 입니다.");

        verify(boardRepository).findWithUsernameById(notExistBoardId);
    }

    @Test
    @DisplayName("게시글 수정 성공")
    void updateBoardSuccess() {
        //given
        given(boardRepository.findWithUsernameById(boardId)).willReturn(Optional.of(board));
        given(board.getMember().getId()).willReturn(memberId);
        willDoNothing().given(board).update(patchBoardRequestDto.getTitle(), patchBoardRequestDto.getContent());

        //when
        ResponseDto response = boardService.updateBoard(patchBoardRequestDto, memberId, boardId);

        //then
        assertThat(response.getCode()).isEqualTo(ResponseCode2.SUCCESS.getValue());
        verify(board).update(patchBoardRequestDto.getTitle(), patchBoardRequestDto.getContent());
    }


    @Test
    @DisplayName("존재하지 않는 게시글 수정 시도")
    void updateBoardNotExistBoard() {
        //given
        Long notExistBoardId = 99999L;
        given(boardRepository.findWithUsernameById(notExistBoardId)).willReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> boardService.updateBoard(patchBoardRequestDto, memberId, notExistBoardId))
                .isInstanceOf(BoardNotFoundException.class)
                .hasMessage("존재하지 않는 게시글 입니다.");
        verify(boardRepository).findWithUsernameById(notExistBoardId);

    }

    @Test
    @DisplayName("게시글 수정 권한 없음")
    void updateBoardNotPermission() {
        //given
        Long differentMemberId = 2L;
        given(boardRepository.findWithUsernameById(boardId)).willReturn(Optional.of(board));
        given(board.getMember().getId()).willReturn(memberId);

        //when

        //then
        assertThatThrownBy(() -> boardService.updateBoard(patchBoardRequestDto, differentMemberId, boardId))
                .isInstanceOf(UnauthorizedUserException.class)
                .hasMessage("삭제할 권한이 없습니다.");
        verify(boardRepository).findWithUsernameById(boardId);
    }


    @Test
    @DisplayName("댓글 0개 이상 일때 게시글 삭제 성공")
    void deleteBoardSuccess() {
        //given
        int commentCount = 2;
        given(boardRepository.findWithUsernameById(boardId)).willReturn(Optional.of(board));
        given(board.getMember().getId()).willReturn(memberId);
        given(commentRepository.countByBoardId(boardId)).willReturn(commentCount);
        willDoNothing().given(commentRepository).deleteAllByBoardId(boardId);


        //when
        ResponseDto response = boardService.deleteBoard(memberId, boardId);

        //then
        verify(boardRepository).findWithUsernameById(boardId);
        verify(commentRepository).countByBoardId(boardId);
        verify(commentRepository).deleteAllByBoardId(boardId);
        verify(board).delete();
        assertThat(response.getCode()).isEqualTo(ResponseCode2.SUCCESS.getValue());
    }

    @Test
    @DisplayName("댓글이 0개 일때 게시글 삭제 성공")
    void deleteBoardCommentCountZeroSuccess() {
        //given
        int zeroCount = 0;
        given(boardRepository.findWithUsernameById(boardId)).willReturn(Optional.of(board));
        given(board.getMember().getId()).willReturn(memberId);
        given(commentRepository.countByBoardId(boardId)).willReturn(zeroCount);

        //when
        ResponseDto response = boardService.deleteBoard(memberId, boardId);


        //then
        verify(boardRepository).findWithUsernameById(boardId);
        verify(commentRepository).countByBoardId(boardId);
        verify(board).delete();
        assertThat(response.getCode()).isEqualTo(ResponseCode2.SUCCESS.getValue());
    }

    @Test
    @DisplayName("게시글 존재하지 않을때 삭제 시도")
    void deleteBoardNotExistBoard() {
        //given
        Long notExistBoardId = 99999L;
        given(boardRepository.findWithUsernameById(notExistBoardId)).willReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> boardService.deleteBoard(memberId, notExistBoardId))
                .isInstanceOf(BoardNotFoundException.class)
                .hasMessage("존재하지 않는 게시글 입니다.");
        verify(boardRepository).findWithUsernameById(notExistBoardId);
    }

    @Test
    @DisplayName("게시글 삭제 권한 없음")
    void deleteBoardNotPermission() {
        //given
        Long differentMemberId = 2L;
        given(boardRepository.findWithUsernameById(boardId)).willReturn(Optional.of(board));
        given(board.getMember().getId()).willReturn(memberId);

        //when

        //then
        assertThatThrownBy(() -> boardService.deleteBoard(differentMemberId, boardId))
                .isInstanceOf(UnauthorizedUserException.class)
                .hasMessage("삭제할 권한이 없습니다.");
        verify(boardRepository).findWithUsernameById(boardId);
    }

    @Test
    @DisplayName("게시글 전체조회 성공")
    void getAllBoardsSuccess() {

        //given
        int offset = (getBoardAllRequestDto.getPage() - 1) * getBoardAllRequestDto.getLimit();
        List<Board> boards = List.of(board, board, board); // List size = 3
        given(boardRepository.findAllWithUsername(getBoardAllRequestDto.getLimit(), offset)).willReturn(boards);

        //when
        ResponseDto response = boardService.getAllBoards(getBoardAllRequestDto);


        //then
        verify(boardRepository).findAllWithUsername(getBoardAllRequestDto.getLimit(), offset);
        assertThat(response.getCode()).isEqualTo(ResponseCode2.SUCCESS.getValue());
        DataResponseDto<List<Board>> dataResponse = (DataResponseDto) response;
        assertThat(dataResponse.getData()).isNotEmpty();
        assertThat(dataResponse.getData().size()).isEqualTo(boards.size());
    }

    @Test
    @DisplayName("게시글 전체조회시 게시글 비어있을때")
    void getAllBoardsNotExistBoard() {
        //given
        int offset = (getBoardAllRequestDto.getPage() - 1) * getBoardAllRequestDto.getLimit();
        List<Board> boards = new ArrayList<>();
        given(boardRepository.findAllWithUsername(getBoardAllRequestDto.getLimit(), offset)).willReturn(boards);

        //when
        ResponseDto response = boardService.getAllBoards(getBoardAllRequestDto);

        //then
        verify(boardRepository).findAllWithUsername(getBoardAllRequestDto.getLimit(), offset);
        assertThat(response.getCode()).isEqualTo(ResponseCode2.SUCCESS.getValue());
        DataResponseDto<List<Board>> dataResponse = (DataResponseDto) response;
        assertThat(dataResponse.getData()).isEmpty();
    }

    @Test
    @DisplayName("검색한 게시글 조회 성공")
    void getSearchBoardSuccess() {
        //given

        String successType = "author";
        String successKeyword = "Author";

        getSearchBoardListRequestDto.setType(successType);
        getSearchBoardListRequestDto.setKeyword(successKeyword);

        List<Board> boards = List.of(board, board, board);
        given(boardRepository.findBoardByTypeAndKeyword(successType, successKeyword)).willReturn(boards);

        //when
        ResponseDto response = boardService.getSearchBoard(getSearchBoardListRequestDto);

        //then
        assertThat(response.getCode()).isEqualTo(ResponseCode2.SUCCESS.getValue());
        verify(boardRepository).findBoardByTypeAndKeyword(successType, successKeyword);
        DataResponseDto<List<Board>> dataResponse = (DataResponseDto) response;
        assertThat(dataResponse.getData()).hasSize(boards.size());
    }

    @Test
    @DisplayName("검색한 게시글 0건 조회")
    void getSearchBoardSuccessNoCount() {
        //given
        String successType = "author";
        String successKeyword = "NotExistResult";

        getSearchBoardListRequestDto.setType(successType);
        getSearchBoardListRequestDto.setKeyword(successKeyword);

        List<Board> boards = new ArrayList<>();
        given(boardRepository.findBoardByTypeAndKeyword(successType, successKeyword)).willReturn(boards);

        //when
        ResponseDto response = boardService.getSearchBoard(getSearchBoardListRequestDto);

        //then
        assertThat(response.getCode()).isEqualTo(ResponseCode2.SUCCESS.getValue());
        verify(boardRepository).findBoardByTypeAndKeyword(successType, successKeyword);
        DataResponseDto<List<Board>> dataResponse = (DataResponseDto) response;
        assertThat(dataResponse.getData()).isEmpty();
    }
}