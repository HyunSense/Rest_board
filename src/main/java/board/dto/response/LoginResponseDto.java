package board.dto.response;

import board.common.ResponseCode;
import board.common.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class LoginResponseDto extends ResponseDto{

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

}
