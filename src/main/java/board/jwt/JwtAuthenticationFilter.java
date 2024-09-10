package board.jwt;

import board.common.ResponseCode2;
import board.config.auth.PrincipalDetails;
import board.dto.request.auth.LoginRequestDto;
import board.dto.response.DataResponseDto;
import board.dto.response.ResponseDto;
import board.dto.response.auth.LoginResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    // 다양한 유형의 인증요청을 처리하기위해 UsernamePasswordAuthenticationFilter 대신
    // AbstractAuthenticationProcessingFilter 사용
    // addFilter 시 순서 확인
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final ObjectMapper objectMapper;

    private static final String DEFAULT_LOGIN_REQUEST_URL = "/api/v1/auth/login";
    private static final String HTTP_METHOD = "POST";
    private static final String BEARER = "Bearer ";

    // 1000 * 1 * 60 * 10 -> 10분
    private static final Long EXPIRED_MS = 1000 * 60L * 10L;

    private final static AntPathRequestMatcher DEFAULT_LOGIN_REQUEST_MATCHER =
            new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD);


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService, ObjectMapper objectMapper) {
        super(DEFAULT_LOGIN_REQUEST_MATCHER);
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.objectMapper = objectMapper;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        log.info("JwtAuthenticationFilter.attemptAuthentication");

        LoginRequestDto loginRequestDto = objectMapper.readValue(request.getReader(), LoginRequestDto.class);
        log.info("LOGIN Attempt = {}", loginRequestDto.getUsername());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        String username = principalDetails.getUsername();
        Long id = principalDetails.getId();

        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority next = iterator.next();

        String role = next.getAuthority();

        String token = jwtTokenService.createJwt(id, username, role, EXPIRED_MS);
        String expired = jwtTokenService.getExpired(token);

        LoginResponseDto dto = new LoginResponseDto(token, expired);
        response.addHeader("Authorization", BEARER + token);
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(response.getWriter(), DataResponseDto.success(dto));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), ResponseDto.failure(ResponseCode2.LOGIN_FAILED));
    }
}
