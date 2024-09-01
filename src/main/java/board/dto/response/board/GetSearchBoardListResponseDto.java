package board.dto.response.board;

import board.common.ResponseCode;
import board.common.ResponseMessage;
import board.dto.response.ResponseDto;
import board.repository.V1.mapper.resultset.BoardResultSet;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
@Setter
public class GetSearchBoardListResponseDto extends ResponseDto {

    private String type;
    private String keyword;
    private int count;
    private List<BoardResultSet> boardList;

    private GetSearchBoardListResponseDto(String type, String keyword, int count, List<BoardResultSet> boardList) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.type = type;
        this.keyword = keyword;
        this.count = count;
        this.boardList = boardList;
    }

    public static ResponseEntity<GetSearchBoardListResponseDto> success(String type, String keyword, int count, List<BoardResultSet> boardList) {

        GetSearchBoardListResponseDto responseBody = new GetSearchBoardListResponseDto(type, keyword, count, boardList);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    // 204는 message body을 포함하지 않음
    public static ResponseEntity<ResponseDto> noResult() {

        ResponseDto responseBody = new ResponseDto(ResponseCode.NOT_EXIST_RESULT, ResponseMessage.NOT_EXIST_RESULT);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
}
