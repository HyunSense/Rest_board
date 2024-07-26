package board.controller;

import board.config.auth.PrincipalDetails;
import board.dto.request.board.*;
import board.dto.response.board.*;
import board.service.BoardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
//@Tag(name = "Board", description = "Board API")
public class BoardController {

    private final BoardService boardService;

    // 글 작성
    @PostMapping("/post")
    public ResponseEntity<? super PostBoardResponseDto> postBoard(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody @Valid PostBoardRequestDto dto) {

        return boardService.createBoard(principalDetails.getId(), dto);
    }

    // 전체 글 목록
    @GetMapping("/posts")
    public ResponseEntity<? super GetBoardAllResponseDto> getBoardAll(@RequestBody(required = false) @Valid GetBoardAllRequestDto dto) {

        if (dto == null) {
            dto = new GetBoardAllRequestDto();
        }

        log.info("page = {}, limit = {}", dto.getPage(), dto.getLimit());
        return boardService.getAllBoards(dto);
    }

    // 글 검색 조회
    @GetMapping("/post/search")
    public ResponseEntity<? super GetSearchBoardListResponseDto> getSearchBoardAll(@ModelAttribute GetSearchBoardListRequestDto dto) {

        return boardService.getSearchBoard(dto);
    }

    // 글 조회
    @GetMapping("/post/{id}")
    public ResponseEntity<? super GetBoardResponseDto> getBoard(@PathVariable Long id) {

        return boardService.getBoardById(id);
    }

    // 글 수정
    @PatchMapping("/post/{id}")
    public ResponseEntity<? super PatchBoardResponseDto> patchBoard(
            @RequestBody @Valid PatchBoardRequestDto dto,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long id) {

        return boardService.updateBoard(dto, principalDetails.getId(), id);
    }

    //글 삭제
    @DeleteMapping("/post/{id}")
    public ResponseEntity<? super DeleteBoardResponseDto> deleteBoard(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long id) {

        return boardService.deleteBoard(principalDetails.getId(), id);
    }

    //댓글리스트 조회
    @GetMapping("/post/{boardId}/comment-list")
    public ResponseEntity<? super GetCommentListResponseDto> getCommentList(@PathVariable Long boardId) {

        return boardService.getCommentList(boardId);
    }

    // 댓글 작성
    @PostMapping("/post/{boardId}/comment")
    public ResponseEntity<? super PostCommentResponseDto> postComment(
            @RequestBody @Valid PostCommentRequestDto dto,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long boardId) {

        return boardService.createComment(dto, principalDetails.getId(), boardId);
    }

    //댓글 삭제
    @DeleteMapping("/post/{boardId}/comment/{id}")
    public ResponseEntity<? super DeleteCommentResponseDto> deleteComment(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long boardId,
            @PathVariable Long id) {

        return boardService.deleteComment(principalDetails.getId(), boardId, id);
    }

    //좋아요 토글
    @GetMapping("/post/{boardId}/likes")
    public ResponseEntity<? super GetLikesResponseDto> toggleLikes(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long boardId) {

        return boardService.toggleLikes(principalDetails.getId(), boardId);
    }
}
