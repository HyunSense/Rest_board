package board.mapper.resultset;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class CommentResultSet {

    private Long id;
    private String username;
    private String content;
    private String createdAt;

    @Builder
    public CommentResultSet(Long id, String username, String content, String createdAt) {
        this.id = id;
        this.username = username;
        this.content = content;
        this.createdAt = createdAt;
    }
}
