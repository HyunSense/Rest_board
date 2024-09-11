package board.dto.response.board.V2;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CommentListDto {

    private int count;
    private List<CommentDto> commentList;

    @Builder
    public CommentListDto(int count, List<CommentDto> commentList) {
        this.count = count;
        this.commentList = commentList;
    }

    public static CommentListDto fromEntity(List<CommentDto> commentList) {
        return CommentListDto.builder()
                .count(commentList.size())
                .commentList(commentList)
                .build();
    }
}
