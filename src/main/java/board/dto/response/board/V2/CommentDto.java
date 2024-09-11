package board.dto.response.board.V2;


import board.entity.V2.Comment;
import lombok.*;

@Getter
public class CommentDto {

    private Long id;
    private String username;
    private String content;
    private String createdAt;

//    @Builder
//    public CommentDto(Comment comment) {
//
//        this.id = comment.getId();
//        this.username = comment.getMember().getUsername();
//        this.content = comment.getContent();
//        this.createdAt = comment.getCreatedAt().toString();
//
//    }

    @Builder
    public CommentDto(Long id, String username, String content, String createdAt) {
        this.id = id;
        this.username = username;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static CommentDto fromEntity(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .username(comment.getMember().getUsername())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt().toString())
                .build();
    }
}
