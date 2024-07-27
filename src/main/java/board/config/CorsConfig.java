package board.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

@Slf4j
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {

        log.info("CorsConfig.corsFilter");

        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // 내서버가 응답을 할 때 json을 javascript 에서 처리할 수 있게 할지를 설정하는 것
        // false 라면 javascript로 요청을 하였을때 응답이 오지 않는다.
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*"); // 모든 ip에 응답을 허용
        config.addAllowedHeader("*"); // 모든 header에 응답을 허용
        config.addAllowedMethod("*"); //모든 method(post, get, put, delete, patch) 요청을 허용
//        config.setMaxAge(3600L);
        config.setExposedHeaders(Collections.singletonList("Authorization"));

        src.registerCorsConfiguration("/api/**", config);

        return new CorsFilter(src);
    }
}
