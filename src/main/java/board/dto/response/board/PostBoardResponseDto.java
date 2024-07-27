package board.dto.response.board;

import board.common.ResponseCode;
import board.common.ResponseMessage;
import board.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class PostBoardResponseDto extends ResponseDto {


    private PostBoardResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<PostBoardResponseDto> success() {

        PostBoardResponseDto postBoardResponseDto = new PostBoardResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(postBoardResponseDto);
    }

    public static ResponseEntity<ResponseDto> notExistUser() {

        ResponseDto responseDto = new ResponseDto(ResponseCode.NOT_EXIST_USER, ResponseMessage.NOT_EXIST_USER);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDto);
    }
}
