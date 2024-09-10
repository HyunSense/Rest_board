package board.dto.response;

import board.common.ResponseCode2;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseDto {

    private String code;
    private String message;
    private HttpStatus status;

    public ResponseDto(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseDto(String code, String message, HttpStatus status) {
        this(code, message);
        this.status = status;
    }

    public static ResponseDto success() {
        return new ResponseDto(ResponseCode2.SUCCESS.getValue(), ResponseCode2.SUCCESS.getDescription());
    }

    public static ResponseDto failure(ResponseCode2 code2) {
        return new ResponseDto(code2.getValue(), code2.getDescription());
    }

    public static ResponseDto failure(ResponseCode2 code2, HttpStatus status) {
        return new ResponseDto(code2.getValue(), code2.getDescription(), status);
    }
}
