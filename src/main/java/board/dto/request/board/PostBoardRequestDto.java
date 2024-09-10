package board.dto.request.board;

import board.entity.V2.Board;
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

    public static Board toEntity(PostBoardRequestDto dto) {

        return Board.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();
    }
}
