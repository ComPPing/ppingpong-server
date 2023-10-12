package co.kr.ppingpong.oauth.kako;

import co.kr.ppingpong.config.auth.JwtProvider;
import co.kr.ppingpong.config.auth.LoginUser;
import co.kr.ppingpong.domain.user.GenderEnum;
import co.kr.ppingpong.domain.user.User;
import co.kr.ppingpong.domain.user.UserRepository;

import co.kr.ppingpong.oauth.dto.kakao.KakaoTokenReqDto;
import co.kr.ppingpong.oauth.dto.kakao.KakaoTokenRespDto;

import lombok.RequiredArgsConstructor;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.Optional;

import static co.kr.ppingpong.dto.user.UserRespDto.*;
import static co.kr.ppingpong.util.CustomResponseUtil.getLoginRespDto;

@RequiredArgsConstructor
@Transactional
@Service
public class KakaoOauth2Service {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Value("${oauth.kakao.client_id}")
    private String KAKAO_CLIENT_ID;

    @Value("${oauth.kakao.redirect_uri}")
    private String KAKAO_REDIRECT_URI;

    public String getAuthCode() {
        String reqUrl = "https://kauth.kakao.com/oauth/authorize?client_id=" + KAKAO_CLIENT_ID
                + "&redirect_uri=" + KAKAO_REDIRECT_URI + "&response_type=code";

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        String response = restTemplate.getForObject(reqUrl, String.class);

        return response;

    }

    public KakaoTokenRespDto getAccessToken(String code) {

        KakaoTokenReqDto kakaoTokenReqDto = KakaoTokenReqDto.builder()
                .client_id(KAKAO_CLIENT_ID)
                .code(code)
//                .redirect_uri("https://ppingpong.pages.dev/callback/kakao")
                .redirect_uri(KAKAO_REDIRECT_URI)
                .grant_type("authorization_code")
                .build();

        KakaoTokenRespDto kakaoTokenRespDto = getToken(kakaoTokenReqDto);

        return kakaoTokenRespDto;
    }

    public LoginUser getUserInfo(KakaoTokenRespDto kakaoTokenRespDto) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + kakaoTokenRespDto.getAccess_token());


        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST, kakaoProfileRequest, String.class);

        //Org.json
        JSONObject jsonObject = new JSONObject(response.getBody());
        System.out.println("json " + jsonObject);
        String email = jsonObject.getJSONObject("kakao_account").getString("email");
        String gender = jsonObject.getJSONObject("kakao_account").getString("gender");
        String age_range = jsonObject.getJSONObject("kakao_account").getString("age_range");
        String age = age_range.substring(0, 2);
        String name = jsonObject.getJSONObject("properties").getString("nickname");

        Optional<User> userOP = userRepository.findByEmail(email);
        if (userOP.isPresent()) {
            LoginUser loginUser = new LoginUser(userOP.get());
            return loginUser;
        } else {
            User user = User.builder()
                    .name(name)
                    .email(email)
                    .gender(gender.equals("male") ? GenderEnum.MALE : GenderEnum.FEMALE)
                    .age(Integer.parseInt(age))
                    .build();
            User userPS = userRepository.save(user);
            LoginUser loginUser = new LoginUser(userPS);
            return loginUser;
        }

    }

    public KakaoTokenRespDto getToken(KakaoTokenReqDto kakaoTokenReqDto) {
        RestTemplate restTemplate = new RestTemplate();
        //  카카오 관련 오류가 body에 null로 담겨서 구체적으로 보기위해 추가했다. + org.apache.httpcomponents:httpclient:4.5 의존성추가
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
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

        return getLoginRespDto(loginUser, jwtProvider);
    }

}
