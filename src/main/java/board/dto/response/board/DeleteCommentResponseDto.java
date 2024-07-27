package board.dto.response.board;

import board.common.ResponseCode;
import board.common.ResponseMessage;
import board.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class DeleteCommentResponseDto extends ResponseDto {

    private DeleteCommentResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<DeleteCommentResponseDto> success() {

        DeleteCommentResponseDto responseBody = new DeleteCommentResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> notExistBoard() {

        ResponseDto responseBody = new ResponseDto(ResponseCode.NOT_EXIST_BOARD, ResponseMessage.NOT_EXIST_BOARD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> notExistComment() {

        ResponseDto responseBody = new ResponseDto(ResponseCode.NOT_EXIST_COMMENT, ResponseMessage.NOT_EXIST_COMMENT);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> notPermission() {

        ResponseDto responseBody = new ResponseDto(ResponseCode.PERMISSION_DENIED, ResponseMessage.PERMISSION_DENIED);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
    }
}
