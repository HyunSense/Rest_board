package board.mapper.resultset;

import lombok.Getter;
import lombok.ToString;

@Getter
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

}
