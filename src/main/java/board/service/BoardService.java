package board.service;

import board.dto.response.PostResponseDto;
import org.springframework.http.ResponseEntity;

public interface BoardService {

    ResponseEntity<PostResponseDto> postBoard(PostResponseDto dto);
}
