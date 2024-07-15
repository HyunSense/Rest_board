package board.dto.response;

import board.common.ResponseCode;
import board.common.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class PostResponseDto extends ResponseDto{

    //TODO: 생성자 범위 public? private?
    public PostResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<PostResponseDto> success() {

        PostResponseDto postResponseDto = new PostResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(postResponseDto);
    }

    public static ResponseEntity<ResponseDto> notExistUser() {

        ResponseDto responseDto = new ResponseDto(ResponseCode.NOT_EXIST_USER, ResponseMessage.NOT_EXIST_USER);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDto);
    }


}
