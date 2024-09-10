package board.dto.response.board.V2;

import board.entity.V2.Board;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDto {

    private Long id;
    private String title;
    private String content;
    private String username;
    private long viewCount;
    private int commentCount;
    private int likesCount;
    private String createdAt;
    private String updatedAt;

    @Builder
    public PostDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.username = board.getMember().getUsername();
        this.viewCount = board.getViewCount();
        this.commentCount = board.getCommentCount();
        this.likesCount = board.getLikesCount();
        this.createdAt = board.getCreatedAt().toString();
        this.updatedAt = board.getUpdatedAt().toString();
    }
}
