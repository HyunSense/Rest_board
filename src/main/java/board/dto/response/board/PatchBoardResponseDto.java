package board.dto.response.board;

import board.common.ResponseCode;
import board.common.ResponseMessage;
import board.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class PatchBoardResponseDto extends ResponseDto {

    private PatchBoardResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<PatchBoardResponseDto> success() {

        PatchBoardResponseDto patchBoardResponseDto = new PatchBoardResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(patchBoardResponseDto);
    }

    public static ResponseEntity<ResponseDto> notExistBoard() {

        ResponseDto responseDto = new ResponseDto(ResponseCode.NOT_EXIST_BOARD, ResponseMessage.NOT_EXIST_BOARD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

    public static ResponseEntity<ResponseDto> notPermission() {

        ResponseDto responseBody = new ResponseDto(ResponseCode.PERMISSION_DENIED, ResponseMessage.PERMISSION_DENIED);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
    }
}
