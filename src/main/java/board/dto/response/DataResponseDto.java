package board.dto.response;

import board.common.ResponseCode2;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DataResponseDto<T> extends ResponseDto {

    T data;

    private DataResponseDto(String code, String message, HttpStatus status, T data) {
        super(code, message, status);
        this.data = data;
    }

    public static <T> DataResponseDto<T> success(T data) {
        return new DataResponseDto<>(ResponseCode2.SUCCESS.getValue(), ResponseCode2.SUCCESS.getDescription(), HttpStatus.OK, data);
    }
}
