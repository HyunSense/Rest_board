package board.mapper.resultset;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CommentListResultSet {

    private String username;
    private String content;
    private String createdAt;
}
