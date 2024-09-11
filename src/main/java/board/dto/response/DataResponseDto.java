package board.dto.response;

import board.common.ResponseCode;
import lombok.Getter;

@Getter
public class DataResponseDto<T> extends ResponseDto {

    T data;

    private DataResponseDto(String code, String message, T data) {
        super(code, message);
        this.data = data;
    }

    public static <T> DataResponseDto<T> success(T data) {
        return new DataResponseDto<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), data);
    }
}
