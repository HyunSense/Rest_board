package board.config.auth;

import board.common.ResponseCode;
import board.dto.response.ResponseDto;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        JWTVerificationException jwtVerificationException = (JWTVerificationException) request.getAttribute("exception");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");


        if (jwtVerificationException instanceof TokenExpiredException) {
            objectMapper.writeValue(response.getWriter(), ResponseDto.failure(ResponseCode.TOKEN_EXPIRED));
            return;
        }

        if (jwtVerificationException != null) {
            objectMapper.writeValue(response.getWriter(), ResponseDto.failure(ResponseCode.INVALID_TOKEN));
            return;
        }

        objectMapper.writeValue(response.getWriter(), ResponseDto.failure(ResponseCode.VALIDATION_FAILED));
    }
}
