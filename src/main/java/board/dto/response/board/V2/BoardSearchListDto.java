package board.dto.response.board.V2;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class BoardSearchListDto {

    private int count;
    private List<BoardDto> boardList;


    @Builder
    protected BoardSearchListDto(int count, List<BoardDto> boardList) {
        this.count = count;
        this.boardList = boardList;
    }

    public static BoardSearchListDto fromEntity(List<BoardDto> boardList) {
        return BoardSearchListDto.builder()
                .count(boardList.size())
                .boardList(boardList)
                .build();

    }
}
