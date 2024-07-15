package board.jwt;

import board.config.auth.PrincipalDetails;
import board.dto.request.auth.LoginRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilterFormType extends UsernamePasswordAuthenticationFilter {
    // 다양한 유형의 인증요청을 처리하기위해 UsernamePasswordAuthenticationFilter 대신
    // AbstractAuthenticationProcessingFilter 사용
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper;

    private static final String DEFAULT_LOGIN_REQUEST_URL = "/login";
    private static final String HTTP_METHOD = "POST";


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        log.info("jwtAuthenticationFilter 로그인 시도");
        String method = request.getMethod();
        log.info("method = {}", method);

//        String username = obtainUsername(request);
//        String password = obtainPassword(request);
//
//        log.info("username = {}", username);
//        log.info("password = {}", password);

        LoginRequestDto loginRequestDto = null;

        try {
            BufferedReader reader = null;
            reader = request.getReader();
            loginRequestDto = objectMapper.readValue(reader, LoginRequestDto.class);
            log.info("loginRequestDto = {}", loginRequestDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        log.info("JwtAuthenticationFilter success");


        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        String username = principalDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority next = iterator.next();

        String role = next.getAuthority();
        log.info("role = {}", role);

        // 1000 * 1 * 60 * 10 -> 10분
        String token = jwtUtil.createJwt(username, role, 1000 * 60L * 10L);

        response.addHeader("Authorization", "Bearer " + token);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        log.info("JwtAuthenticationFilter fail");
        PrintWriter out = response.getWriter();
        out.println("LOGIN - FAIL");

        response.setStatus(401);
    }
}
