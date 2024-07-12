package board.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
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

//    private SecretKey secretKey;

    @Value("${spring.jwt.secret}")
    private String secretKey;

    public void setSecretKey(String secretKey) {

        this.secretKey = secretKey;
    }


//    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
//
//        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Algorithm.HMAC256(secret).toString());
//        log.info("secretKey = {}", secretKey);
//    }

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

    public String createJwt(String username, String role, Long expiredMs) {

        log.info("expired = {}", new Date(System.currentTimeMillis() + expiredMs));

        return JWT.create()
                .withSubject("JWT토큰")
                .withClaim("username", username)
                .withClaim("role", role)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + expiredMs))
                .sign(Algorithm.HMAC256(secretKey));
    }

}
