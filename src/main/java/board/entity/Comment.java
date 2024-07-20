package board.entity;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class Comment {

    private Long id;
    private Long memberId;
    private Long boardId;
    private String content;
    private LocalDateTime createdAt;
    private int isDeleted;
}
