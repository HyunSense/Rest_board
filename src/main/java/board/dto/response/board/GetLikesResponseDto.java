package board.dto.response.board;

import board.common.ResponseCode;
import board.dto.response.ResponseDto;
import lombok.Getter;

@Getter
public class GetLikesResponseDto extends ResponseDto {

    private GetLikesResponseDto() {
        super(ResponseCode.SUCCESS, ResponseCode.SUCCESS);
    }


}
