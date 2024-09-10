package board.service;

import board.dto.request.board.PostCommentRequestDto;
import board.dto.response.ResponseDto;

public interface CommentService {

    ResponseDto createComment(PostCommentRequestDto dto, Long memberId, Long boardId);
    ResponseDto deleteComment(Long memberId, Long boardId, Long id);
    ResponseDto getCommentList(Long boardId);

}
