package board.controller;

import board.config.auth.PrincipalDetails;
import board.dto.request.board.*;
import board.dto.response.ResponseDto;
import board.service.BoardService;
import board.service.CommentService;
import board.service.LikesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    private final CommentService commentService;
    private final LikesService likesService;

    // 글 작성
    @PostMapping("/boards")
    public ResponseEntity<ResponseDto> postBoard(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody @Valid PostBoardRequestDto dto) {

        ResponseDto response = boardService.createBoard(principalDetails.getId(), dto);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 글 조회
    @GetMapping("/boards/{id}")
    public ResponseEntity<ResponseDto> getBoard(@PathVariable Long id) {

        ResponseDto response = boardService.getBoardById(id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 전체 글 조회
    @GetMapping("/boards")
    public ResponseEntity<ResponseDto> getBoardAll(@ModelAttribute @Valid GetBoardAllRequestDto dto) {

        if (dto == null) {
            dto = new GetBoardAllRequestDto();
        }

        log.info("page = {}, limit = {}", dto.getPage(), dto.getLimit());

        ResponseDto response = boardService.getAllBoards(dto);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 글 검색 조회
    @GetMapping("/boards/search")
    public ResponseEntity<ResponseDto> getSearchBoardAll(@ModelAttribute @Valid GetSearchBoardListRequestDto dto) {

        ResponseDto response = boardService.getSearchBoard(dto);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // 글 수정
    @PatchMapping("/boards/{id}")
    public ResponseEntity<ResponseDto> patchBoard(
            @RequestBody @Valid PatchBoardRequestDto dto,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long id) {

        ResponseDto response = boardService.updateBoard(dto, principalDetails.getId(), id);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    //글 삭제
    @DeleteMapping("/boards/{id}")
    public ResponseEntity<ResponseDto> deleteBoard(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long id) {

        ResponseDto response = boardService.deleteBoard(principalDetails.getId(), id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //댓글리스트 조회
    @GetMapping("/boards/{boardId}/comments")
    public ResponseEntity<ResponseDto> getCommentList(@PathVariable Long boardId) {

        ResponseDto response = commentService.getCommentList(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 댓글 작성
    @PostMapping("/boards/{boardId}/comments")
    public ResponseEntity<ResponseDto> postComment(
            @RequestBody @Valid PostCommentRequestDto dto,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long boardId) {

        ResponseDto response = commentService.createComment(dto, principalDetails.getId(), boardId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //댓글 삭제
    @DeleteMapping("/boards/{boardId}/comments/{id}")
    public ResponseEntity<ResponseDto> deleteComment(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long boardId,
            @PathVariable Long id) {

        ResponseDto response = commentService.deleteComment(principalDetails.getId(), boardId, id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //좋아요 토글
    @GetMapping("/boards/{boardId}/likes")
    public ResponseEntity<ResponseDto> toggleLikes(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long boardId) {

        ResponseDto response = likesService.toggleLikes(principalDetails.getId(), boardId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
