package board.dto.response.board.V2;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class BoardListDto {

    private int page;
    private int limit;
    private List<BoardDto> boardList;

    @Builder
    protected BoardListDto(int page, int limit, List<BoardDto> boardList) {
        this.page = page;
        this.limit = limit;
        this.boardList = boardList;
    }

    public static BoardListDto fromEntity(int page, int limit, List<BoardDto> boardList) {
        return BoardListDto.builder()
                .page(page)
                .limit(limit)
                .boardList(boardList)
                .build();

    }
}
