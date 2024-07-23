package board.config;

import board.config.auth.CustomAuthenticationEntryPoint;
import board.jwt.JWTUtil;
import board.jwt.JwtAuthenticationFilter;
import board.jwt.JwtAuthorizationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {


    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final JWTUtil jwtUtil;
    private final CorsFilter corsFilter;
    private final ObjectMapper objectMapper;

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .authorizeHttpRequests(req -> req
//                        .requestMatchers("/posts/**").hasAnyRole("USER", "ADMIN")
//                        .requestMatchers("/user").hasRole("USER")
//                        .requestMatchers("/admin").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/api/v1/post/{postId:\\d+}").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/post/{id}", "/api/v1/posts", "/api/v1/post/search", "/api/v1/post/{boardId}/comment-list").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/swagger-resources/**").permitAll()
                        .requestMatchers("/api/v1/post/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .anyRequest().authenticated())
                .addFilter(corsFilter)
                .addFilterAt(new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration), jwtUtil, objectMapper), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthorizationFilter(jwtUtil, objectMapper), JwtAuthenticationFilter.class);
            //TODO: AbstractAuthenticationProcessingFilter 핕터 실행 순서 확인

        // AuthenticationEntryPoint : 인증되지 않은 사용자가 인증이 필요한 앤드포인트로 접근할때
        // AccessDeniedHandler : 인증은 완료되었으나 앤드포인트에 접근할 권한이 없을때
        http
                .exceptionHandling(e -> e.authenticationEntryPoint(customAuthenticationEntryPoint));

        return http.build();
    }
}
