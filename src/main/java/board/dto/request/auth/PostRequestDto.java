package board.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class PostRequestDto {

    // null, 공백, 빈문자열 x
    @NotBlank
    private String title;

    @NotBlank
    private String content;
}
