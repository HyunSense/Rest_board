package board.dto.request.board;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCommentRequestDto {

    @NotBlank
    private String content;
}
