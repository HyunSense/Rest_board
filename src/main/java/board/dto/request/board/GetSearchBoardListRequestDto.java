package board.dto.request.board;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetSearchBoardListRequestDto {

    @NotBlank
    String type;

    @NotBlank
    String keyword;
}
