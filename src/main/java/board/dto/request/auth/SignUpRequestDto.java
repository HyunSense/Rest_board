package board.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SignUpRequestDto {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String name;

    //TODO: required 여부 체크
    private String email;

}
