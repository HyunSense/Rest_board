package board.mapper.resultset;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class BoardResultSet {

    private Long id;
    private String title;
    private String content;
    private String author;
    private long viewCount;
    private int commentCount;
    private int likesCount;
    private String createdAt;
    private String updatedAt;

    @Builder
    public BoardResultSet(Long id, String title, String content, String author, long viewCount, int commentCount, int likesCount, String createdAt, String updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.likesCount = likesCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
