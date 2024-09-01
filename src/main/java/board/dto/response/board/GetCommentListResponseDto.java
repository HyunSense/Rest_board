package board.dto.response.board;

import board.common.ResponseCode;
import board.common.ResponseMessage;
import board.dto.response.ResponseDto;
import board.repository.V1.mapper.resultset.CommentResultSet;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
@Setter
public class GetCommentListResponseDto extends ResponseDto {

    private int count;
    private List<CommentResultSet> commentList;

    private GetCommentListResponseDto(List<CommentResultSet> commentList, int count) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.count = count;
        this.commentList = commentList;
    }

    public static ResponseEntity<GetCommentListResponseDto> success(List<CommentResultSet> commentList, int count) {

        GetCommentListResponseDto responseBody = new GetCommentListResponseDto(commentList, count);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> notExistBoard() {

        ResponseDto responseDto = new ResponseDto(ResponseCode.NOT_EXIST_BOARD, ResponseMessage.NOT_EXIST_BOARD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }
}
