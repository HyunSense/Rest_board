package board.dto.response.board;

import board.common.ResponseCode;
import board.common.ResponseMessage;
import board.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class GetSearchBoardListResponseDto extends ResponseDto {

    private GetSearchBoardListResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<GetSearchBoardListResponseDto> success() {

        GetSearchBoardListResponseDto responseBody = new GetSearchBoardListResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
