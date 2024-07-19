package board.dto.response.auth;

import board.common.ResponseCode;
import board.common.ResponseMessage;
import board.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class LoginResponseDto extends ResponseDto {

    private final String token;
    private final String expired;

    private LoginResponseDto(String token, String expired) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.token = token;
        this.expired = expired;
    }

    public static ResponseEntity<LoginResponseDto> success(String token, String expired) {

        LoginResponseDto loginResponseDto = new LoginResponseDto(token, expired);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponseDto);
    }

    public static ResponseEntity<ResponseDto> loginFailed() {
        ResponseDto responseDto = new ResponseDto(ResponseCode.LOGIN_FAILED, ResponseMessage.LOGIN_FAILED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDto);
    }

}
