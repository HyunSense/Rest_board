package board.entity.V1;

import board.dto.request.board.PatchBoardRequestDto;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class Board {

    private Long id;
    private Long memberId;
    private String title;
    private String content;
    private long viewCount;
    private int commentCount;
    private int likesCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int isDeleted;

    public void patchBoard(PatchBoardRequestDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void increaseComment() {
        this.commentCount++;
    }

    public void decreaseComment() {
        this.commentCount--;
    }

    public void increaseLikes(){
        this.likesCount++;
    }

    public void decreaseLikes() {
        this.likesCount--;
    }
}
