package co.kr.ppingpong.oauth;

import co.kr.ppingpong.config.auth.LoginUser;
import co.kr.ppingpong.dto.ResponseDto;
import co.kr.ppingpong.oauth.dto.google.GoogleTokenRespDto;
import co.kr.ppingpong.oauth.dto.kakao.KakaoTokenRespDto;
import co.kr.ppingpong.oauth.google.GoogleOauth2Service;
import co.kr.ppingpong.oauth.kako.KakaoOauth2Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static co.kr.ppingpong.dto.user.UserRespDto.*;

@RequiredArgsConstructor
@RestController
public class Oauth2Controller {

    private final KakaoOauth2Service kakaoOauth2Service;
    private final GoogleOauth2Service googleOauth2Service;

    // 프론트에서 인가코드를 받음 -> 인가코드와함께 post 요청을 보내줄 컨트롤러
    @GetMapping("/login/kakao")
    ResponseEntity<?> login_kakao(@RequestParam String code) {

        KakaoTokenRespDto kakaoTokenRespDto = kakaoOauth2Service.getAccessToken(code);
        LoginUser loginUser = kakaoOauth2Service.getUserInfo(kakaoTokenRespDto);
        LoginRespDto loginRespDto = kakaoOauth2Service.login(loginUser);

        return new ResponseEntity<>(new ResponseDto<>(1, "로그인 성공", loginRespDto), HttpStatus.OK);
    }

    @GetMapping("/login/google")
    ResponseEntity<?> login_google(@RequestParam String code) throws JsonProcessingException {
        GoogleTokenRespDto googleTokenRespDto = googleOauth2Service.getAccessToken(code);
        LoginUser loginUser = googleOauth2Service.getUserInfo(googleTokenRespDto);
        LoginRespDto loginRespDto = googleOauth2Service.login(loginUser);

        return new ResponseEntity<>(new ResponseDto<>(1, "로그인 성공", loginRespDto), HttpStatus.OK);
    }
}
