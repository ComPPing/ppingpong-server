package co.kr.ppingpong.oauth.google;


import co.kr.ppingpong.config.auth.JwtProvider;
import co.kr.ppingpong.config.auth.LoginUser;
import co.kr.ppingpong.domain.user.User;
import co.kr.ppingpong.domain.user.UserRepository;
import co.kr.ppingpong.dto.user.UserRespDto;
import co.kr.ppingpong.oauth.dto.google.GoogleLoginDto;
import co.kr.ppingpong.oauth.dto.google.GoogleTokenReqDto;
import co.kr.ppingpong.oauth.dto.google.GoogleTokenRespDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.util.Optional;

import static co.kr.ppingpong.dto.user.UserRespDto.*;

@RequiredArgsConstructor
@Transactional
@Service
public class GoogleOauth2Service {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Value("oauth.google.client_id")
    private String GOOGLE_CLIENT_ID;

    @Value("oauth.google.redirect_uri")
    private String GOOGLE_REDIRECT_URI;

    public GoogleTokenRespDto getAccessToken(String code) {

        GoogleTokenReqDto googleTokenReqDto = GoogleTokenReqDto.builder()
                .clientId(GOOGLE_CLIENT_ID)
                .code(code)
                .redirectUri(GOOGLE_REDIRECT_URI)
                .grantType("authorization_code")
                .build();

        GoogleTokenRespDto googleTokenRespDto = getToken(googleTokenReqDto);
        return googleTokenRespDto;
    }

    public LoginUser getUserInfo(GoogleTokenRespDto googleTokenRespDto) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        ObjectMapper objectMapper = new ObjectMapper();
        String requestUrl = UriComponentsBuilder.fromHttpUrl("https://oauth2.googleapis.com/tokeninfo").queryParam("id_token", googleTokenRespDto.getId_token()).toUriString();
        String resultJson = restTemplate.getForObject(requestUrl, String.class);

        GoogleLoginDto googleLoginDto = objectMapper.readValue(resultJson, GoogleLoginDto.class);
        Optional<User> userOP = userRepository.findByEmail(googleLoginDto.getEmail());
        if (userOP.isPresent()) {
            LoginUser loginUser = new LoginUser(userOP.get());
            return loginUser;
        } else {
            User user = User.builder()
                    .name(googleLoginDto.getName())
                    .email(googleLoginDto.getEmail())
                    .build();
            User userPS = userRepository.save(user);
            LoginUser loginUser = new LoginUser(userPS);
            return loginUser;
        }
    }

    private GoogleTokenRespDto getToken(GoogleTokenReqDto googleTokenReqDto) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        GoogleTokenRespDto googleTokenRespDto = restTemplate.postForObject("https://oauth2.googleapis.com/token", googleTokenReqDto, GoogleTokenRespDto.class);
        return googleTokenRespDto;
    }

    public LoginRespDto login(LoginUser loginUser) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtAccessToken = jwtProvider.accessTokenCreate(loginUser);

        HttpHeaders headers = new HttpHeaders(); // 응답헤더랑 responseDto 둘다 jwtAccessToken 넣어줌(뭐로 줄지 안정해서)
        headers.add("ACCESS_HEADER", jwtAccessToken);

        LoginRespDto loginRespDto = new LoginRespDto(loginUser.getUser(), jwtAccessToken);
        return loginRespDto;
    }
}
