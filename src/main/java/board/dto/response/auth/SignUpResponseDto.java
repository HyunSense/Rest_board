package board.dto.response.auth;

import board.common.ResponseCode;
import board.common.ResponseMessage;
import board.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class SignUpResponseDto extends ResponseDto {


    private SignUpResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<SignUpResponseDto> success() {

        SignUpResponseDto signUpResponseDto = new SignUpResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(signUpResponseDto);
    }

    public static ResponseEntity<ResponseDto> existLoginId() {

        ResponseDto responseDto = new ResponseDto(ResponseCode.DUPLICATE_USERNAME, ResponseMessage.DUPLICATE_USERNAME);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

    public static ResponseEntity<ResponseDto> existEmail() {

        ResponseDto responseDto = new ResponseDto(ResponseCode.DUPLICATE_EMAIL, ResponseMessage.DUPLICATE_EMAIL);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }
}
