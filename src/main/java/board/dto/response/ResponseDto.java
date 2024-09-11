package board.dto.response;

import board.common.ResponseCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseDto {

    private String code;
    private String message;

    @JsonIgnore
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
        return new ResponseDto(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), HttpStatus.OK);
    }

    public static ResponseDto failure(ResponseCode code2) {
        return new ResponseDto(code2.getCode(), code2.getMessage());
    }

    public static ResponseDto failure(ResponseCode code2, HttpStatus status) {
        return new ResponseDto(code2.getCode(), code2.getMessage(), status);
    }
}
