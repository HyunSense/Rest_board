package board.dto.response.board.V2;

import board.entity.V2.Board;
import lombok.*;

@Getter
public class BoardDto {

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
    protected BoardDto(Long id, String title, String content, String username, long viewCount, int commentCount, int likesCount, String createdAt, String updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.username = username;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.likesCount = likesCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static BoardDto fromEntity(Board board) {
        return BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .username(board.getMember().getUsername())
                .viewCount(board.getViewCount())
                .commentCount(board.getCommentCount())
                .likesCount(board.getLikesCount())
                .createdAt(board.getCreatedAt().toString())
                .updatedAt(board.getUpdatedAt().toString())
                .build();
    }
}
