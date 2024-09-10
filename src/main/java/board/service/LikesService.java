package board.service;

import board.dto.response.ResponseDto;

public interface LikesService {

    ResponseDto toggleLikes(Long memberId, Long boardId);

}
