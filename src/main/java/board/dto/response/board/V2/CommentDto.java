package board.dto.response.board.V2;


import board.entity.V2.Comment;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentDto {

    private Long id;
    private String username;
    private String content;
    private String createdAt;

    @Builder
    public CommentDto(Comment comment) {

        this.id = comment.getId();
        this.username = comment.getMember().getUsername();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt().toString();

    }
}
