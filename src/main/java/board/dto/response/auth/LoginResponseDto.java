package board.dto.response.auth;

import lombok.Getter;

@Getter
public class LoginResponseDto {

    private final String token;
    private final String expired;

    public LoginResponseDto(String token, String expired) {

        this.token = token;
        this.expired = expired;
    }
}
