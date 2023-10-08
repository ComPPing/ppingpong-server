package co.kr.ppingpong.oauth;

import co.kr.ppingpong.config.auth.JwtProvider;
import co.kr.ppingpong.config.auth.LoginUser;
import co.kr.ppingpong.domain.user.User;
import co.kr.ppingpong.domain.user.UserRepository;

import co.kr.ppingpong.oauth.dto.kakao.KakaoLoginDto;
import co.kr.ppingpong.oauth.dto.kakao.KakaoTokenReqDto;
import co.kr.ppingpong.oauth.dto.kakao.KakaoTokenRespDto;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.Optional;

import static co.kr.ppingpong.dto.user.UserRespDto.*;

@RequiredArgsConstructor
@Transactional
@Service
public class KakaoOauth2Service {

    private final ObjectMapper om;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Value("oauth.kakao.client_id")
    private String KAKAO_CLIENT_ID;

    @Value("oauth.kakao.redirect_uri")
    private String KAKAO_REDIRECT_URI;

    public KakaoTokenRespDto getAccessToken(String code) {

        KakaoTokenReqDto kakaoTokenReqDto = KakaoTokenReqDto.builder()
                .client_id(KAKAO_CLIENT_ID)
                .code(code)
                .redirect_uri(KAKAO_REDIRECT_URI)
                .grant_type("authorization_code")
                .build();

        KakaoTokenRespDto kakaoTokenRespDto = getToken(kakaoTokenReqDto);

        return kakaoTokenRespDto;
    }

    public LoginUser getUserInfo(KakaoTokenRespDto kakaoTokenRespDto) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + kakaoTokenRespDto.getAccess_token());

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\"]");

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);
        KakaoLoginDto kaKaoLoginDto = restTemplate.postForObject("https://kapi.kakao.com/v2/user/me", request, KakaoLoginDto.class);

        Optional<User> userOP = userRepository.findByEmail(kaKaoLoginDto.getEmail());
        if (userOP.isPresent()) {
            LoginUser loginUser = new LoginUser(userOP.get());
            return loginUser;
        } else {
            User user = User.builder()
                    .name(kaKaoLoginDto.getKakaoAccount().getKakaoProfile().getNickname())
                    .email(kaKaoLoginDto.getKakaoAccount().getEmail())
                    .build();
            User userPS = userRepository.save(user);
            LoginUser loginUser = new LoginUser(userPS);
            return loginUser;
        }

    }

    public KakaoTokenRespDto getToken(KakaoTokenReqDto kakaoTokenReqDto) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        String[] fieldNames = {"client_id", "grant_type", "redirect_uri", "code"};
        for (String fieldName : fieldNames) {
            String fieldValue = getFieldByName(kakaoTokenReqDto, fieldName);
            body.add(fieldName, fieldValue);
        }

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        return restTemplate.postForObject("https://kauth.kakao.com/oauth/token", request, KakaoTokenRespDto.class);
    }

    private String getFieldByName(KakaoTokenReqDto kakaoTokenReqDto, String fieldName) {
        switch (fieldName) {
            case "client_id":
                return kakaoTokenReqDto.getClient_id();
            case "grant_type":
                return "authorization_code";
            case "redirect_uri":
                return kakaoTokenReqDto.getRedirect_uri();
            case "code":
                return kakaoTokenReqDto.getCode();
            default:
                return "";
        }
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
