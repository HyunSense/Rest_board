package board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDTO {

    // TODO: auto_increment 고유값 id 따로?
    private String postNumber;
    private String title;
    private String content;
    private String author;
    private int viewCount;
    private String createdAt;

}
