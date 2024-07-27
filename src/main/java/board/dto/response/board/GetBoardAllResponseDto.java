package board.dto.response.board;

import board.common.ResponseCode;
import board.common.ResponseMessage;
import board.dto.response.ResponseDto;
import board.mapper.resultset.BoardResultSet;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
@Setter
public class GetBoardAllResponseDto extends ResponseDto {

    private int page;
    private int limit;
    private List<BoardResultSet> boardList;

    private GetBoardAllResponseDto(int page, int limit, List<BoardResultSet> boardList) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.page = page;
        this.limit = limit;
        this.boardList = boardList;
    }

    public static ResponseEntity<GetBoardAllResponseDto> success(int page, int limit, List<BoardResultSet> boardList) {

        GetBoardAllResponseDto responseBody = new GetBoardAllResponseDto(page, limit, boardList);

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> notExistBoard() {

        ResponseDto responseBody = new ResponseDto(ResponseCode.NOT_EXIST_BOARD, ResponseMessage.NOT_EXIST_BOARD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

}
