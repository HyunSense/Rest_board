package board.service;

import board.dto.request.board.*;
import board.dto.response.ResponseDto;

public interface BoardService {

    ResponseDto createBoard(Long memberId, PostBoardRequestDto dto);
    ResponseDto getBoardById(Long id);
    ResponseDto getAllBoards(GetBoardAllRequestDto dto);
    ResponseDto getSearchBoard(GetSearchBoardListRequestDto dto);
    ResponseDto updateBoard(PatchBoardRequestDto dto, Long memberId, Long id);
    ResponseDto deleteBoard(Long memberId, Long id);
//    ResponseDtoV2 createComment(PostCommentRequestDto dto, Long memberId, Long boardId);
//    ResponseDtoV2 deleteComment(Long memberId, Long boardId, Long id);
//    ResponseDtoV2 getCommentList(Long boardId);
//    ResponseDtoV2 toggleLikes(Long memberId, Long boardId);

}
