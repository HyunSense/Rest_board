package board.dto.request.board;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetBoardAllRequestDto {

    private final static int DEFAULT_PAGE = 1;
    private final static int DEFAULT_LIMIT = 10;

    @Min(1)
    @Max(10)
    private Integer page;

    @Min(1)
    @Max(10)
    private Integer limit;

    public GetBoardAllRequestDto() {

        this.page = DEFAULT_PAGE;
        this.limit = DEFAULT_LIMIT;
    }
}
