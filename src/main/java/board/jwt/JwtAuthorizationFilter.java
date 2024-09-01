package board.jwt;

import board.config.auth.PrincipalDetails;
import board.entity.V1.Member;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter{

    private final JwtTokenService jwtTokenService;
    private final ObjectMapper objectMapper;
//    private static final String BEARER = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("JwtAuthorizationFilter.doFilterInternal");

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        // header가 있는지 확인
        if (authorization == null || !authorization.startsWith("Bearer")) {

            if (authorization == null) {
                log.info("Authorization header is missing.");
            } else {
                log.info("Authorization header does not start with 'Bearer '.");
            }

            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader(HttpHeaders.AUTHORIZATION).replace("Bearer ", "");

        try {

            jwtTokenService.isExpired(token);

        } catch (JWTVerificationException e) {

            request.setAttribute("exception", e);
            filterChain.doFilter(request, response);
            return;
        }

        Long id = jwtTokenService.getId(token);
        String username = jwtTokenService.getUsername(token);
        String role = jwtTokenService.getRole(token);

        Member member = Member.builder()
                .id(id)
                .username(username)
                .role(role)
                .build();

        PrincipalDetails principalDetails = new PrincipalDetails(member);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}
