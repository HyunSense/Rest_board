package board.dto.response;

import board.common.ResponseCode;
import board.common.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
public class ResponseDto {

    private String code;
    private String message;

    public static ResponseEntity<ResponseDto> databaseError() {

        ResponseDto responseBody = new ResponseDto(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> validationFailed() {

        ResponseDto responseBody = new ResponseDto(ResponseCode.VALIDATION_FAILED, ResponseMessage.VALIDATION_FAILED);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> tokenExpired() {

        ResponseDto responseBody = new ResponseDto(ResponseCode.TOKEN_EXPIRED, ResponseMessage.TOKEN_EXPIRED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> invalidToken() {

        ResponseDto responseBody = new ResponseDto(ResponseCode.INVALID_TOKEN, ResponseMessage.INVALID_TOKEN);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }

}
