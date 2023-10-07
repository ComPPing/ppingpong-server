package co.kr.ppingpong.config;

import co.kr.ppingpong.config.auth.JwtAuthorizationFilter;
import co.kr.ppingpong.config.auth.OAuth2DetailsService;
import co.kr.ppingpong.config.auth.OAuth2SuccessHandler;
import co.kr.ppingpong.util.CustomResponseUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final OAuth2DetailsService oAuth2DetailsService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        log.debug("filterChain 등록");
        http.headers().frameOptions().disable();
        http.csrf().disable();
        http.cors().configurationSource(configurationSource());
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.formLogin().disable();
        http.httpBasic().disable();

        http.authorizeRequests()
                .antMatchers("/api/s/**").authenticated()
                .anyRequest().permitAll();

//        http.oauth2Login().userInfoEndpoint().userService(oAuth2DetailsService);
//        http.oauth2Login().successHandler(oAuth2SuccessHandler);

        http.apply(new CustomSecurityFilterManager());

        // 인증실패 예외 가로채기
        http.exceptionHandling().authenticationEntryPoint((request, response, e) -> {
            log.error("error : " + e.getMessage());
            CustomResponseUtil.fail(response, "로그인을 진행 해주세요 .", HttpStatus.UNAUTHORIZED);
        });

        // 권한실패 예외 가로채기
        http.exceptionHandling().accessDeniedHandler((request, response, e) -> {
            log.error("error : " + e.getMessage());
            CustomResponseUtil.fail(response, "권한이 없습니다.", HttpStatus.FORBIDDEN);
        });


        return http.build();

    }

    // 필터 등록
    public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception { // 스프링 시큐리티에서 사용되는 구성을 위한 빌더 클래스
            builder.addFilter(jwtAuthorizationFilter);
            super.configure(builder);
        }
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(Arrays.asList(
                googleClientRegistration(), kakaoClientRegistration()
        ));
    }

    @Bean
    public ClientRegistration googleClientRegistration() {
        return ClientRegistration.withRegistrationId("google")
                .clientId("547894052834-qhjibkbfe1v2e7l6fe9bm87qihs1lhqp.apps.googleusercontent.com")
                .clientSecret("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")
                .clientName("Google")
                .scope("profile", "email")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://www.googleapis.com/oauth2/v4/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName("sub")
                .redirectUri("https://ppingpong.store/login/oauth2/code/google")
//                .redirectUri("http://localhost:8080/login/oauth2/code/google")
                .clientName("Google")
                .build();
    }

    @Bean
    public ClientRegistration kakaoClientRegistration() {
        return ClientRegistration.withRegistrationId("kakao")
                .clientId("96f7ce1905bc319224b20c051f0fb1e1")
                .clientName("Kakao")
                .scope("profile_nickname", "account_email")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationUri("https://kauth.kakao.com/oauth/authorize")
                .tokenUri("https://kauth.kakao.com/oauth/token")
                .userInfoUri("https://kapi.kakao.com/v2/user/me")
                .userNameAttributeName("id")
                .redirectUri("https://ppingpong.store/login/oauth2/code/kakao")
//                .redirectUri("http://localhost:8080/login/oauth2/code/kakao")
                .build();
    }

    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedOriginPattern("*"); // 모든 ip 주소 허용 (프론트 엔드 IP만 허용 react)
        configuration.setAllowCredentials(true); // 클라이언트의 쿠키 요청 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 주소요청에 위 설정을 넣어주겠다.
        return source;
    }
}
