package board.dto.request.board;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PostBoardRequestDto {

    // null, 공백, 빈문자열 x
    @NotBlank
    private String title;

    @NotBlank
    private String content;
}
