package board.jwt;

import com.auth0.jwt.exceptions.TokenExpiredException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JwtTokenServiceTest {


    JwtTokenService jwtTokenService;
    String token;

//    @BeforeEach
    @BeforeAll
    void setUp() {

        jwtTokenService = new JwtTokenService();
        jwtTokenService.setSecretKey("this_is_secretKey");

        Long expiredMs = 60000 * 5L; // 5m
        token = jwtTokenService.createJwt(1L, "jaehoon1022", "ROLE_USER", expiredMs);
        System.out.println("token = " + token);

    }

    @Test
    void getUsername() {

        String username = jwtTokenService.getUsername(token);
        Assertions.assertThat(username).isEqualTo("jaehoon1022");
    }

    @Test
    void getRole() {

        String role = jwtTokenService.getRole(token);
        Assertions.assertThat(role).isEqualTo("ROLE_USER");

    }

    @Test
    void isExpired() {
        Boolean expired = jwtTokenService.isExpired(token);
        Assertions.assertThat(expired).isFalse();

        JwtTokenService jwt = new JwtTokenService();
        jwt.setSecretKey("secret");
        Long expiredMs = 1L; // 1ms
        String expiredToken = jwt.createJwt(1L, "hyun", "ROLE_USER", expiredMs);

        Assertions.assertThatThrownBy(() -> jwt.isExpired(expiredToken))
                .isInstanceOf(TokenExpiredException.class);

    }

}