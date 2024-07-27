package board.dto.response.board;

import board.common.ResponseCode;
import board.common.ResponseMessage;
import board.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class PostCommentResponseDto extends ResponseDto {

    private PostCommentResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<PostCommentResponseDto> success() {

        PostCommentResponseDto postCommentResponseDto = new PostCommentResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(postCommentResponseDto);
    }

    public static ResponseEntity<ResponseDto> notExistBoard() {

        ResponseDto responseDto = new ResponseDto(ResponseCode.NOT_EXIST_BOARD, ResponseMessage.NOT_EXIST_BOARD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

    public static ResponseEntity<ResponseDto> notPermission() {

        ResponseDto responseBody = new ResponseDto(ResponseCode.PERMISSION_DENIED, ResponseMessage.PERMISSION_DENIED);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
    }
}
