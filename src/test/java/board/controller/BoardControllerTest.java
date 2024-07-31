package board.controller;

import board.common.ResponseCode;
import board.common.ResponseMessage;
import board.dto.request.auth.SignUpRequestDto;
import board.dto.request.board.*;
import board.dto.response.board.GetBoardResponseDto;
import board.jwt.JwtTokenService;
import board.mapper.AutoIncrementMapper;
import board.service.BoardService;
import board.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AutoIncrementMapper autoIncrementMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private JwtTokenService jwtTokenService;
    private String token;
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    @BeforeEach
    void setUp() {

        autoIncrementMapper.resetMemberAutoIncrement();
        autoIncrementMapper.resetBoardAutoIncrement();
        autoIncrementMapper.resetCommentAutoIncrement();

        Long id = 1L;
        String username = "test";
        String role = "ROLE_USER";
        Long expiredMs = 1000 * 60L; // 1분
        token = jwtTokenService.createJwt(id, username, role, expiredMs);

        SignUpRequestDto signUpRequestDto = new SignUpRequestDto();

        signUpRequestDto.setUsername("testMember");
        signUpRequestDto.setPassword("testPassword");
        signUpRequestDto.setName("testName");
        signUpRequestDto.setEmail("test@test.com");
        memberService.signUp(signUpRequestDto);

        PostBoardRequestDto postBoardRequestDto = new PostBoardRequestDto();
        postBoardRequestDto.setTitle("test title");
        postBoardRequestDto.setContent("test content");

        boardService.createBoard(1L, postBoardRequestDto);

        PostCommentRequestDto postCommentRequestDto = new PostCommentRequestDto();
        postCommentRequestDto.setContent("test comment");

        boardService.createComment(postCommentRequestDto, 1L, 1L);
    }

    @AfterEach
    void destroy() {


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
                post("/api/v1/post")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, BEARER + token)
                        .content(objectMapper.writeValueAsString(postBoardRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(ResponseMessage.SUCCESS))
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
                        post("/api/v1/post")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + invalidToken)
                                .content(objectMapper.writeValueAsString(postBoardRequestDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.INVALID_TOKEN))
                .andExpect(jsonPath("$.message").value(ResponseMessage.INVALID_TOKEN))
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
                        post("/api/v1/post")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token)
                                .content(objectMapper.writeValueAsString(postBoardRequestDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.TOKEN_EXPIRED))
                .andExpect(jsonPath("$.message").value(ResponseMessage.TOKEN_EXPIRED))
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
                        post("/api/v1/post")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token)
                                .content(objectMapper.writeValueAsString(postBoardRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_FAILED))
                .andExpect(jsonPath("$.message").value(ResponseMessage.VALIDATION_FAILED))
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
                        post("/api/v1/post")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token)
                                .content(objectMapper.writeValueAsString(postBoardRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_FAILED))
                .andExpect(jsonPath("$.message").value(ResponseMessage.VALIDATION_FAILED))
                .andDo(print());
    }

    @Test
    @DisplayName("전체 글 목록 조회 성공")
    void getBoardAllSuccess() throws Exception{
        //given
        int page = 1;
        int limit = 10;
        GetBoardAllRequestDto getBoardAllRequestDto = new GetBoardAllRequestDto();
        getBoardAllRequestDto.setPage(page);
        getBoardAllRequestDto.setLimit(limit);

        //when
        mockMvc.perform(
                        get("/api/v1/posts")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token)
                                .content(objectMapper.writeValueAsString(getBoardAllRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(ResponseMessage.SUCCESS))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.limit").value(10))
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
                        get("/api/v1/posts")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(ResponseMessage.SUCCESS))
                .andExpect(jsonPath("$.page").value(page))
                .andExpect(jsonPath("$.limit").value(limit))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 조회 성공")
    void getBoardByIdSuccess() throws Exception{
        //given
        Long boardId = 1L;

        //when
        mockMvc.perform(
                        get("/api/v1/post/" + boardId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(ResponseMessage.SUCCESS))
                .andExpect(jsonPath("$.title").value("test title"))
                .andExpect(jsonPath("$.content").value("test content"))
                .andDo(print());
    }

    @Test
    @DisplayName("없는 게시글 조회시 실패")
    void getBoardByIdFailedNoExistBoard() throws Exception{
        //given
        Long boardId = 999L;

        //when
        mockMvc.perform(
                        get("/api/v1/post/" + boardId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.NOT_EXIST_BOARD))
                .andExpect(jsonPath("$.message").value(ResponseMessage.NOT_EXIST_BOARD))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정 성공")
    void patchBoardSuccess() throws Exception{
        //given
        Long boardId = 1L;

        PatchBoardRequestDto patchBoardRequestDto = new PatchBoardRequestDto();
        patchBoardRequestDto.setTitle("update title");
        patchBoardRequestDto.setContent("update content");

        //when
        mockMvc.perform(
                        patch("/api/v1/post/"+boardId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token)
                                .content(objectMapper.writeValueAsString(patchBoardRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(ResponseMessage.SUCCESS))
                .andDo(print());
    }
    
    @Test
    @DisplayName("게시글 일부 수정 성공")
    void patchBoardSuccessUpdateTitle() throws Exception{
        //given
        Long boardId = 1L;
        PatchBoardRequestDto patchBoardRequestDto = new PatchBoardRequestDto();
        patchBoardRequestDto.setTitle("update title no content");

        //when
        mockMvc.perform(
                        patch("/api/v1/post/"+boardId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token)
                                .content(objectMapper.writeValueAsString(patchBoardRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(ResponseMessage.SUCCESS))
                .andDo(print());
    }

    @Test
    @DisplayName("body 없이 게시글 수정시 실패")
    void patchBoardFailedNoBody() throws Exception{
        //given
        Long boardId = 1L;

        //when
        mockMvc.perform(
                        patch("/api/v1/post/"+boardId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_FAILED))
                .andExpect(jsonPath("$.message").value(ResponseMessage.VALIDATION_FAILED))
                .andDo(print());
    }

    @Test
    @DisplayName("권한 없는 사용자 게시글 수정시 실패")
    void patchBoardFailedNotPermission() throws Exception{
        //given
        Long boardId = 1L;
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
                        patch("/api/v1/post/"+boardId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token)
                                .content(objectMapper.writeValueAsString(patchBoardRequestDto)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ResponseCode.PERMISSION_DENIED))
                .andExpect(jsonPath("$.message").value(ResponseMessage.PERMISSION_DENIED))
                .andDo(print());
        //then
    }

    @Test
    @DisplayName("게시글 삭제 성공")
    void deleteBoardSuccess() throws Exception{
        //given
        Long boardId = 1L;

        //when
        mockMvc.perform(
                        delete("/api/v1/post/"+boardId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(ResponseMessage.SUCCESS))
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 삭제 ")
    void deleteBoardFailedNotExistBoard() throws Exception{
        //given
        Long boardId = 999L;
        //when
        mockMvc.perform(
                        delete("/api/v1/post/"+boardId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.NOT_EXIST_BOARD))
                .andExpect(jsonPath("$.message").value(ResponseMessage.NOT_EXIST_BOARD))
                .andDo(print());
    }

    @Test
    @DisplayName("권한 없는 사용자 게시글 삭제시 실패")
    void deleteBoardFailedNotPermission() throws Exception{
        //given
        Long boardId = 1L;
        Long id = 999L;
        String username = "notPermissionMember";
        String role = "ROLE_USER";
        Long expiredMs = 1000 * 60L; // 1분
        token = jwtTokenService.createJwt(id, username, role, expiredMs);

        //when
        mockMvc.perform(
                        delete("/api/v1/post/"+boardId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ResponseCode.PERMISSION_DENIED))
                .andExpect(jsonPath("$.message").value(ResponseMessage.PERMISSION_DENIED))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글에 댓글 작성 성공")
    void createCommentSuccess() throws Exception{
        //given
        Long boardId = 1L;
        PostCommentRequestDto postCommentRequestDto = new PostCommentRequestDto();
        postCommentRequestDto.setContent("test comment");

        //when
        mockMvc.perform(
                        post("/api/v1/post/"+ boardId + "/comment")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token)
                                .content(objectMapper.writeValueAsString(postCommentRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(ResponseMessage.SUCCESS))
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
                        post("/api/v1/post/"+ boardId + "/comment")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token)
                                .content(objectMapper.writeValueAsString(postCommentRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.NOT_EXIST_BOARD))
                .andExpect(jsonPath("$.message").value(ResponseMessage.NOT_EXIST_BOARD))
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void deleteCommentSuccess() throws Exception{
        //given
        Long boardId = 1L;
        Long commentId = 1L;

        //when
        mockMvc.perform(
                        delete("/api/v1/post/"+ boardId + "/comment/" + commentId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(ResponseMessage.SUCCESS))
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
                        delete("/api/v1/post/"+ boardId + "/comment/" + commentId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.NOT_EXIST_BOARD))
                .andExpect(jsonPath("$.message").value(ResponseMessage.NOT_EXIST_BOARD))
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 댓글 삭제 실패")
    void deleteCommentFailedNotExistComment() throws Exception{
        //given
        Long boardId = 1L;
        Long commentId = 999L;

        //when
        mockMvc.perform(
                        delete("/api/v1/post/"+ boardId + "/comment/" + commentId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.NOT_EXIST_COMMENT))
                .andExpect(jsonPath("$.message").value(ResponseMessage.NOT_EXIST_COMMENT))
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

        Long boardId = 1L;
        Long commentId = 1L;

        //when
        mockMvc.perform(
                        delete("/api/v1/post/"+ boardId + "/comment/" + commentId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ResponseCode.PERMISSION_DENIED))
                .andExpect(jsonPath("$.message").value(ResponseMessage.PERMISSION_DENIED))
                .andDo(print());
    }

    @Test
    @DisplayName("해당 게시글 댓글 목록 가져오기 성공")
    void getCommentListSuccess() throws Exception{
        //given
        Long boardId = 1L;

        //when
        mockMvc.perform(
                        get("/api/v1/post/"+ boardId + "/comment-list")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(ResponseMessage.SUCCESS))
                .andExpect(jsonPath("$.count").value(1))
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
                        get("/api/v1/post/search")
                                .accept(MediaType.APPLICATION_JSON)
                                .queryParam("type", type)
                                .queryParam("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(ResponseMessage.SUCCESS))
                .andExpect(jsonPath("$.count").value(1))
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
                        get("/api/v1/post/search")
                                .accept(MediaType.APPLICATION_JSON)
                                .queryParam("type", type)
                                .queryParam("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(ResponseMessage.SUCCESS))
                .andExpect(jsonPath("$.count").value(0))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글에 좋아요 추가 성공")
    void toggleLikesSuccess() throws Exception{
        //given
        Long boardId = 1L;

        //when
        mockMvc.perform(
                        get("/api/v1/post/" + boardId + "/likes")
                                .accept(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(ResponseMessage.SUCCESS))
                .andDo(print());

        //then
        ResponseEntity<? super GetBoardResponseDto> response =
                boardService.getBoardById(1L);
        GetBoardResponseDto responseBody = (GetBoardResponseDto) response.getBody();
        assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getLikesCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("게시글에 좋아요 삭제 성공")
    void toggleLikesSuccessDeleteLikes() throws Exception{
        //given
        Long memberId = 1L;
        Long boardId = 1L;
        boardService.toggleLikes(memberId, boardId);

        //when
        mockMvc.perform(
                        get("/api/v1/post/" + boardId + "/likes")
                                .accept(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(ResponseMessage.SUCCESS))
                .andDo(print());

        //then
        ResponseEntity<? super GetBoardResponseDto> response =
                boardService.getBoardById(1L);
        GetBoardResponseDto responseBody = (GetBoardResponseDto) response.getBody();
        assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getLikesCount()).isEqualTo(0);
    }
}

