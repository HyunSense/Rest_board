package board.dto.request.board;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetBoardAllRequestDto {

    private int page;
    private int limit;
}
