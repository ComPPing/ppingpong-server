package co.kr.ppingpong.oauth.google;


import co.kr.ppingpong.config.auth.JwtProvider;
import co.kr.ppingpong.config.auth.LoginUser;
import co.kr.ppingpong.domain.user.GenderEnum;
import co.kr.ppingpong.domain.user.User;
import co.kr.ppingpong.domain.user.UserRepository;

import co.kr.ppingpong.oauth.dto.google.GoogleTokenReqDto;
import co.kr.ppingpong.oauth.dto.google.GoogleTokenRespDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

import static co.kr.ppingpong.dto.user.UserRespDto.*;
import static co.kr.ppingpong.util.CustomResponseUtil.getLoginRespDto;

@RequiredArgsConstructor
@Transactional
@Service
public class GoogleOauth2Service {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Value("${oauth.google.client_id}")
    private String GOOGLE_CLIENT_ID;

    @Value("${oauth.google.redirect_uri}")
    private String GOOGLE_REDIRECT_URI;

    public GoogleTokenRespDto getAccessToken(String code) {

        GoogleTokenReqDto googleTokenReqDto = GoogleTokenReqDto.builder()
                .clientId(GOOGLE_CLIENT_ID)
                .clientSecret("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")
                .code(code)
                .redirectUri(GOOGLE_REDIRECT_URI)
//                .redirectUri("http://localhost:8080/login/google")
//                .redirectUri("https://ppingpong.pages.dev/callback/google")
                .grantType("authorization_code")
                .build();

        GoogleTokenRespDto googleTokenRespDto = getToken(googleTokenReqDto);
        return googleTokenRespDto;
    }

    public LoginUser getUserInfo(GoogleTokenRespDto googleTokenRespDto) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + googleTokenRespDto.getAccess_token());

        HttpEntity<MultiValueMap<String, String>> googleProfileRequest = new HttpEntity<>(httpHeaders);

        String requestUrl = UriComponentsBuilder.fromHttpUrl("https://oauth2.googleapis.com/tokeninfo").queryParam("id_token", googleTokenRespDto.getId_token()).toUriString();
        ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, googleProfileRequest, String.class);

        JSONObject jsonObject = new JSONObject(response.getBody());
        System.out.println("json " + jsonObject);
        String account_id = jsonObject.getString("sub");
        String name = jsonObject.getString("name");
        String email = jsonObject.getString("email");

        ResponseEntity<String> responsePeople = restTemplate.exchange("https://people.googleapis.com/v1/people/"
                +account_id+"?personFields=genders,birthdays", HttpMethod.GET, googleProfileRequest, String.class);
        JSONObject jsonObject_People = new JSONObject(responsePeople.getBody());
        System.out.println("jsonObject_People " + jsonObject_People);

        JSONArray genders = jsonObject_People.getJSONArray("genders");
        JSONObject genderObj = genders.getJSONObject(0);
        String gender = genderObj.getString("formattedValue");

        JSONArray birthdays = jsonObject_People.getJSONArray("birthdays");
        JSONObject birthdayObj = birthdays.getJSONObject(0);
        JSONObject dateObj = birthdayObj.getJSONObject("date");
        int year = dateObj.getInt("year");

        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();

        Optional<User> userOP = userRepository.findByEmail(email);
        if (userOP.isPresent()) {
            LoginUser loginUser = new LoginUser(userOP.get());
            return loginUser;
        } else {
            User user = User.builder()
                    .name(name)
                    .email(email)
                    .age(currentYear - year)
                    .gender(gender.equals("Male") ? GenderEnum.MALE : GenderEnum.FEMALE)
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

        return getLoginRespDto(loginUser, jwtProvider);
    }


}
