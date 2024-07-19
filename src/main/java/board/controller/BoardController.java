package board.controller;

import board.config.auth.PrincipalDetails;
import board.dto.request.board.GetBoardAllRequestDto;
import board.dto.request.board.PatchBoardRequestDto;
import board.dto.request.board.PostBoardRequestDto;
import board.dto.response.board.*;
import board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class BoardController {

    private final BoardService boardService;

    //TODO: PECS 공식
    //TODO: 외부의 데이터를 생산(PRODUCER)한다면 <? extends ResponseDto>와 같은 패턴이 적합합니다.
    //TODO: 외부의 데이터를 소비(CONSUMER)한다면 <? super PostResponseDto>와 같은 패턴이 적합합니다.

    // 글 작성
    @PostMapping("/post")
    public ResponseEntity<? super PostBoardResponseDto> postBoard(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody @Valid PostBoardRequestDto postBoardRequestDto) {

        return boardService.createBoard(principalDetails.getId(), postBoardRequestDto);
    }

    // 전체 글 목록
    @GetMapping("/posts")
    public ResponseEntity<? super GetBoardAllResponseDto> getBoardAll(@RequestBody GetBoardAllRequestDto getBoardAllRequestDto) {

        return boardService.getAllBoards(getBoardAllRequestDto);
    }

    // 글 조회
    @GetMapping("/post/{id}")
    public ResponseEntity<? super GetBoardResponseDto> getBoard(@PathVariable Long id) {

        return boardService.getBoardById(id);
    }

    // 글 수정
    @PatchMapping("/post/{id}")
    public ResponseEntity<? super PatchBoardResponseDto> patchBoard(@RequestBody @Valid PatchBoardRequestDto dto, @AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable Long id) {

        return boardService.updateBoard(dto, principalDetails.getId(), id);
    }

    //글 삭제
    @DeleteMapping("/post/{id}")
    public ResponseEntity<? super DeleteBoardResponseDto> deleteBoard(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable Long id) {

        return boardService.deleteBoard(principalDetails.getId(), id);
    }
}
