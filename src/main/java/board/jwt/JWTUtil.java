package board.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JWTUtil {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    // 테스트용
    public void setSecretKey(String secretKey) {

        this.secretKey = secretKey;
    }

    public Long getId(String token) {

        return JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token)
                .getClaim("id")
                .asLong();

    }

    public String getUsername(String token) {

        return JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token)
                .getClaim("username")
                .asString();

    }

    public String getRole(String token) {

        return JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token)
                .getClaim("role")
                .asString();
    }

    public Boolean isExpired(String token) {

        return JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token)
                .getExpiresAt().before(new Date());

    }

    public String getExpired(String token) {
        return JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token)
                .getExpiresAt().toString();
    }

    public String createJwt(Long id, String username, String role, Long expiredMs) {

//        log.info("expired = {}", new Date(System.currentTimeMillis() + expiredMs));

        return JWT.create()
                .withSubject("JWT토큰")
                .withClaim("id", id)
                .withClaim("username", username)
                .withClaim("role", role)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + expiredMs))
                .sign(Algorithm.HMAC256(secretKey));
    }

}
