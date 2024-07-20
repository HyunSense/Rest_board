package board.mapper.resultset;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GetBoardResultSet {

    private Long id;
    private String title;
    private String content;
    private String author;
//    private Long viewCount;
//    private int commentCount;
    private String createdAt;
    private String updatedAt;

}
