package board.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginRequestDto {

    //TODO: @NotBlank 의 필요성??? (현재 로그인은 모두 필터에서 작동하는데 필요가있나?)
    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
