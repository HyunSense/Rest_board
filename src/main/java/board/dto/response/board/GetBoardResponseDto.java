package board.dto.response.board;

import board.common.ResponseCode;
import board.common.ResponseMessage;
import board.dto.response.ResponseDto;
import board.mapper.resultset.GetBoardResultSet;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@ToString
public class GetBoardResponseDto extends ResponseDto {

    private Long id;
    private String title;
    private String content;
    private String author;
    private Long viewCount;
    private String createdAt;
    private String updatedAt;

    private GetBoardResponseDto(GetBoardResultSet resultSet) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);

        this.id = resultSet.getId();
        this.title = resultSet.getTitle();
        this.content = resultSet.getContent();
        this.author = resultSet.getAuthor();
        this.viewCount = resultSet.getViewCount();
        this.createdAt = resultSet.getCreatedAt();
        this.updatedAt = resultSet.getUpdatedAt();
    }

    public static ResponseEntity<GetBoardResponseDto> success(GetBoardResultSet resultSet) {

        GetBoardResponseDto responseBody = new GetBoardResponseDto(resultSet);

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }


    public static ResponseEntity<ResponseDto> notExistBoard() {

        ResponseDto responseBody = new ResponseDto(ResponseCode.NOT_EXIST_BOARD, ResponseMessage.NOT_EXIST_BOARD);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
