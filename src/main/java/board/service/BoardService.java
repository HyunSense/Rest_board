package board.service;

import board.dto.request.board.GetBoardAllRequestDto;
import board.dto.request.board.PatchBoardRequestDto;
import board.dto.request.board.PostBoardRequestDto;
import board.dto.response.board.*;
import org.springframework.http.ResponseEntity;

public interface BoardService {

    ResponseEntity<? super PostBoardResponseDto> createBoard(Long memberId, PostBoardRequestDto dto);
    ResponseEntity<? super GetBoardAllResponseDto> getAllBoards(GetBoardAllRequestDto getBoardAllRequestDto);
    ResponseEntity<? super GetBoardResponseDto> getBoardById(Long id);

    ResponseEntity<? super PatchBoardResponseDto> updateBoard(PatchBoardRequestDto patchBoardRequestDto, Long memberId, Long id);
    ResponseEntity<? super DeleteBoardResponseDto> deleteBoard(Long memberId, Long id);

}
