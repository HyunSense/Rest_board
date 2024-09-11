package board.controller;

import board.common.ResponseCode;
import board.dto.request.auth.SignUpRequestDto;
import board.dto.request.board.PatchBoardRequestDto;
import board.dto.request.board.PostBoardRequestDto;
import board.dto.request.board.PostCommentRequestDto;
import board.dto.response.DataResponseDto;
import board.dto.response.ResponseDto;
import board.dto.response.board.V2.BoardDto;
import board.entity.V2.Board;
import board.entity.V2.Comment;
import board.entity.V2.Member;
import board.jwt.JwtTokenService;
import board.repository.BoardRepository;
import board.repository.CommentRepository;
import board.repository.MemberRepository;
import board.service.BoardService;
import board.service.CommentService;
import board.service.LikesService;
import board.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class BoardControllerTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private BoardService boardService;
    
    @Autowired
    private CommentService commentService;

    @Autowired
    private LikesService likesService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private JwtTokenService jwtTokenService;
    private String token;
    private static Long boardId;
    private static Long memberId;
    private static Long commentId;

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    @BeforeEach
    void setUp() {


        SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
        signUpRequestDto.setUsername("test");
        signUpRequestDto.setPassword("testPassword");
        signUpRequestDto.setName("testName");
        signUpRequestDto.setEmail("test@test.com");
        memberService.signUp(signUpRequestDto);
        Member member = memberRepository.findByUsername("test").orElseThrow();
        memberId = member.getId();

        PostBoardRequestDto postBoardRequestDto = new PostBoardRequestDto();
        postBoardRequestDto.setTitle("test title");
        postBoardRequestDto.setContent("test content");

        boardService.createBoard(member.getId(), postBoardRequestDto);
        List<Board> boards = boardRepository.findAll();
        Board board = boards.get(0);
        boardId = board.getId();

        PostCommentRequestDto postCommentRequestDto = new PostCommentRequestDto();
        postCommentRequestDto.setContent("test comment");

        commentService.createComment(postCommentRequestDto, memberId, boardId);
        List<Comment> comments = commentRepository.findAllByBoardId(boardId);
        Comment comment = comments.get(0);
        commentId = comment.getId();

//        Long id = 1L;
        String username = "test";
        String role = "ROLE_USER";
        Long expiredMs = 1000 * 60L; // 1분
        token = jwtTokenService.createJwt(memberId, username, role, expiredMs);
    }

    @AfterEach
    void destroy() {
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("인증된 사용자 게시글 작성 성공")
    void postBoardSuccess() throws Exception{
        //given
        Long memberId = 1L;
        PostBoardRequestDto postBoardRequestDto = new PostBoardRequestDto();
        postBoardRequestDto.setTitle("test title");
        postBoardRequestDto.setContent("test content");

        //when
        mockMvc.perform(
                post("/api/v1/boards")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, BEARER + token)
                        .content(objectMapper.writeValueAsString(postBoardRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(ResponseCode.SUCCESS.getMessage()))
                .andDo(print());

    }

    @Test
    @DisplayName("인증되지 않은 사용자 게시글 작성 실패")
    void postBoardFailedInvalidToken() throws Exception{
        //given
        PostBoardRequestDto postBoardRequestDto = new PostBoardRequestDto();
        postBoardRequestDto.setTitle("test title");
        postBoardRequestDto.setContent("test content");

        String invalidToken = "invalid token";

        //when
        mockMvc.perform(
                        post("/api/v1/boards")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + invalidToken)
                                .content(objectMapper.writeValueAsString(postBoardRequestDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.INVALID_TOKEN.getCode()))
                .andExpect(jsonPath("$.message").value(ResponseCode.INVALID_TOKEN.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("만료된 사용자 게시글 작성 실패")
    void postBoardFailedExpiredToken() throws Exception{
        //given
        PostBoardRequestDto postBoardRequestDto = new PostBoardRequestDto();
        postBoardRequestDto.setTitle("test title");
        postBoardRequestDto.setContent("test content");


        Long id = 1L;
        String username = "test";
        String role = "ROLE_USER";
        Long expiredToken = 1L;
        token = jwtTokenService.createJwt(id, username, role, expiredToken);

        //when
        mockMvc.perform(
                        post("/api/v1/boards")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token)
                                .content(objectMapper.writeValueAsString(postBoardRequestDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.TOKEN_EXPIRED.getCode()))
                .andExpect(jsonPath("$.message").value(ResponseCode.TOKEN_EXPIRED.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("title 필드 누락 게시글 작성 실패")
    void postBoardFailedTitleFieldIsMissing() throws Exception{
        //given
        PostBoardRequestDto postBoardRequestDto = new PostBoardRequestDto();
        postBoardRequestDto.setTitle("no content field only title");

        //when
        mockMvc.perform(
                        post("/api/v1/boards")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token)
                                .content(objectMapper.writeValueAsString(postBoardRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_FAILED.getCode()))
                .andExpect(jsonPath("$.message").value(ResponseCode.VALIDATION_FAILED.getMessage()))
                .andDo(print());

    }

    @Test
    @DisplayName("content 필드 누락 게시글 작성 실패")
    void postBoardFailedContentFieldIsMissing() throws Exception{
        //given
        PostBoardRequestDto postBoardRequestDto = new PostBoardRequestDto();
        postBoardRequestDto.setContent("no title field only content");

        //when
        mockMvc.perform(
                        post("/api/v1/boards")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token)
                                .content(objectMapper.writeValueAsString(postBoardRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_FAILED.getCode()))
                .andExpect(jsonPath("$.message").value(ResponseCode.VALIDATION_FAILED.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("전체 글 목록 조회 성공")
    void getBoardAllSuccess() throws Exception{
        //given
        int page = 1;
        int limit = 10;


        //when
        mockMvc.perform(
                        get("/api/v1/boards")
                                .accept(MediaType.APPLICATION_JSON)
                                .queryParam("page", String.valueOf(page))
                                .queryParam("limit", String.valueOf(limit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(ResponseCode.SUCCESS.getMessage()))
                .andExpect(jsonPath("$.data.page").value(1))
                .andExpect(jsonPath("$.data.limit").value(10))
                .andDo(print());
    }

    @Test
    @DisplayName("body 데이터 없이 전체 글 목록 조회 성공")
    void getBoardSuccessNoBody() throws Exception{
        //given
        int page = 1;
        int limit = 10;

        //when
        mockMvc.perform(
                        get("/api/v1/boards")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(ResponseCode.SUCCESS.getMessage()))
                .andExpect(jsonPath("$.data.page").value(page))
                .andExpect(jsonPath("$.data.limit").value(limit))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 조회 성공")
    void getBoardByIdSuccess() throws Exception{
        //given
//        // Long boardId = 1L;

        //when
        mockMvc.perform(
                        get("/api/v1/boards/" + boardId)
                                .accept(MediaType.APPLICATION_JSON))
//                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(ResponseCode.SUCCESS.getMessage()))
                .andExpect(jsonPath("$.data.title").value("test title"))
                .andExpect(jsonPath("$.data.content").value("test content"))
                .andDo(print());
    }

    @Test
    @DisplayName("없는 게시글 조회시 실패")
    void getBoardByIdFailedNoExistBoard() throws Exception{
        //given
        Long boardId = 999L;

        //when
        mockMvc.perform(
                        get("/api/v1/boards/" + boardId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.NOT_EXIST_BOARD.getCode()))
                .andExpect(jsonPath("$.message").value(ResponseCode.NOT_EXIST_BOARD.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정 성공")
    void patchBoardSuccess() throws Exception{
        //given
        // Long boardId = 1L;

        PatchBoardRequestDto patchBoardRequestDto = new PatchBoardRequestDto();
        patchBoardRequestDto.setTitle("update title");
        patchBoardRequestDto.setContent("update content");

        //when
        mockMvc.perform(
                        patch("/api/v1/boards/"+boardId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token)
                                .content(objectMapper.writeValueAsString(patchBoardRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(ResponseCode.SUCCESS.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 일부 수정 성공")
    void patchBoardSuccessUpdateTitle() throws Exception{
        //given
        // Long boardId = 1L;
        PatchBoardRequestDto patchBoardRequestDto = new PatchBoardRequestDto();
        patchBoardRequestDto.setTitle("update title no content");

        //when
        mockMvc.perform(
                        patch("/api/v1/boards/"+boardId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token)
                                .content(objectMapper.writeValueAsString(patchBoardRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(ResponseCode.SUCCESS.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("body 없이 게시글 수정시 실패")
    void patchBoardFailedNoBody() throws Exception{
        //given
        // Long boardId = 1L;

        //when
        mockMvc.perform(
                        patch("/api/v1/boards/"+boardId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_FAILED.getCode()))
                .andExpect(jsonPath("$.message").value(ResponseCode.VALIDATION_FAILED.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("권한 없는 사용자 게시글 수정시 실패")
    void patchBoardFailedNotPermission() throws Exception{
        //given
        // Long boardId = 1L;
        Long id = 999L;
        String username = "notPermissionMember";
        String role = "ROLE_USER";
        Long expiredMs = 1000 * 60L; // 1분
        token = jwtTokenService.createJwt(id, username, role, expiredMs);


        PatchBoardRequestDto patchBoardRequestDto = new PatchBoardRequestDto();
        patchBoardRequestDto.setTitle("update title");
        patchBoardRequestDto.setContent("update content");

        //when
        mockMvc.perform(
                        patch("/api/v1/boards/"+boardId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token)
                                .content(objectMapper.writeValueAsString(patchBoardRequestDto)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ResponseCode.PERMISSION_DENIED.getCode()))
                .andExpect(jsonPath("$.message").value(ResponseCode.PERMISSION_DENIED.getMessage()))
                .andDo(print());
        //then
    }

    @Test
    @DisplayName("게시글 삭제 성공")
    void deleteBoardSuccess() throws Exception{
        //given
        // Long boardId = 1L;

        //when
        mockMvc.perform(
                        delete("/api/v1/boards/"+boardId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(ResponseCode.SUCCESS.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 삭제 ")
    void deleteBoardFailedNotExistBoard() throws Exception{
        //given
        Long boardId = 999L;
        //when
        mockMvc.perform(
                        delete("/api/v1/boards/"+boardId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.NOT_EXIST_BOARD.getCode()))
                .andExpect(jsonPath("$.message").value(ResponseCode.NOT_EXIST_BOARD.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("권한 없는 사용자 게시글 삭제시 실패")
    void deleteBoardFailedNotPermission() throws Exception{
        //given
        // Long boardId = 1L;
        Long id = 999L;
        String username = "notPermissionMember";
        String role = "ROLE_USER";
        Long expiredMs = 1000 * 60L; // 1분
        token = jwtTokenService.createJwt(id, username, role, expiredMs);

        //when
        mockMvc.perform(
                        delete("/api/v1/boards/"+boardId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ResponseCode.PERMISSION_DENIED.getCode()))
                .andExpect(jsonPath("$.message").value(ResponseCode.PERMISSION_DENIED.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글에 댓글 작성 성공")
    void createCommentSuccess() throws Exception{
        //given
        // Long boardId = 1L;
        PostCommentRequestDto postCommentRequestDto = new PostCommentRequestDto();
        postCommentRequestDto.setContent("test comment");

        //when
        mockMvc.perform(
                        post("/api/v1/boards/"+ boardId + "/comments")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token)
                                .content(objectMapper.writeValueAsString(postCommentRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(ResponseCode.SUCCESS.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글에 댓글 작성 실패")
    void createCommentFailedNotExistBoard() throws Exception{
        //given
        Long boardId = 999L;
        PostCommentRequestDto postCommentRequestDto = new PostCommentRequestDto();
        postCommentRequestDto.setContent("no board");


        //when
        mockMvc.perform(
                        post("/api/v1/boards/"+ boardId + "/comments")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token)
                                .content(objectMapper.writeValueAsString(postCommentRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.NOT_EXIST_BOARD.getCode()))
                .andExpect(jsonPath("$.message").value(ResponseCode.NOT_EXIST_BOARD.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void deleteCommentSuccess() throws Exception{
        //given
        // Long boardId = 1L;
//        Long commentId = 1L;

        //when
        mockMvc.perform(
                        delete("/api/v1/boards/"+ boardId + "/comments/" + commentId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(ResponseCode.SUCCESS.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 댓글 삭제 실패")
    void deleteCommentFailedNotExistBoard() throws Exception{
        //given
        Long boardId = 999L;
        Long commentId = 1L;

        //when
        mockMvc.perform(
                        delete("/api/v1/boards/"+ boardId + "/comments/" + commentId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.NOT_EXIST_BOARD.getCode()))
                .andExpect(jsonPath("$.message").value(ResponseCode.NOT_EXIST_BOARD.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 댓글 삭제 실패")
    void deleteCommentFailedNotExistComment() throws Exception{
        //given
        // Long boardId = 1L;
        Long commentId = 999L;

        //when
        mockMvc.perform(
                        delete("/api/v1/boards/"+ boardId + "/comments/" + commentId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.NOT_EXIST_COMMENT.getCode()))
                .andExpect(jsonPath("$.message").value(ResponseCode.NOT_EXIST_COMMENT.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 삭제 권한 없음")
    void deleteCommentFailedNotPermission() throws Exception{
        //given
        Long id = 999L;
        String username = "notPermissionMember";
        String role = "ROLE_USER";
        Long expiredMs = 1000 * 60L; // 1분
        token = jwtTokenService.createJwt(id, username, role, expiredMs);

        // Long boardId = 1L;
//        Long commentId = 1L;

        //when
        mockMvc.perform(
                        delete("/api/v1/boards/"+ boardId + "/comments/" + commentId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ResponseCode.PERMISSION_DENIED.getCode()))
                .andExpect(jsonPath("$.message").value(ResponseCode.PERMISSION_DENIED.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("해당 게시글 댓글 목록 가져오기 성공")
    void getCommentListSuccess() throws Exception{
        //given
        // Long boardId = 1L;

        //when
        mockMvc.perform(
                        get("/api/v1/boards/"+ boardId + "/comments")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(ResponseCode.SUCCESS.getMessage()))
                .andExpect(jsonPath("$.data.count").value(1))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 검색 조회 성공")
    void getSearchBoardSuccess() throws Exception{
        //given
        String type = "title";
        String keyword = "test";

        //when
        mockMvc.perform(
                        get("/api/v1/boards/search")
                                .accept(MediaType.APPLICATION_JSON)
                                .queryParam("type", type)
                                .queryParam("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(ResponseCode.SUCCESS.getMessage()))
                .andExpect(jsonPath("$.data.count").value(1))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 검색 조회 결과 0건")
    void getSearchBoardSuccessNoResult() throws Exception{
        //given
        String type = "title";
        String keyword = "no Result";

        //when
        mockMvc.perform(
                        get("/api/v1/boards/search")
                                .accept(MediaType.APPLICATION_JSON)
                                .queryParam("type", type)
                                .queryParam("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(ResponseCode.SUCCESS.getMessage()))
                .andExpect(jsonPath("$.data.count").value(0))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글에 좋아요 추가 성공")
    void toggleLikesSuccess() throws Exception{
        //given
        // Long boardId = 1L;

        //when
        mockMvc.perform(
                        get("/api/v1/boards/" + boardId + "/likes")
                                .accept(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(ResponseCode.SUCCESS.getMessage()))
                .andDo(print());

        //then
        ResponseDto response = boardService.getBoardById(boardId);
        DataResponseDto<BoardDto> dataResponse = (DataResponseDto<BoardDto>) response;
        assertThat(dataResponse).isNotNull();
        assertThat(dataResponse.getData().getLikesCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("게시글에 좋아요 삭제 성공")
    void toggleLikesSuccessDeleteLikes() throws Exception{
        //given
//        Long memberId = 1L;
        // Long boardId = 1L;
        likesService.toggleLikes(memberId, boardId);

        //when
        mockMvc.perform(
                        get("/api/v1/boards/" + boardId + "/likes")
                                .accept(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(ResponseCode.SUCCESS.getMessage()))
                .andDo(print());

        //then
        ResponseDto response = boardService.getBoardById(boardId);
        DataResponseDto<BoardDto> dataResponse = (DataResponseDto<BoardDto>) response;

        assertThat(dataResponse.getData()).isNotNull();
        assertThat(dataResponse.getData().getLikesCount()).isEqualTo(0);
    }
}

