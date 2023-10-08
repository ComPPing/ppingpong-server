package co.kr.ppingpong.oauth;

import co.kr.ppingpong.config.auth.LoginUser;
import co.kr.ppingpong.domain.user.User;
import co.kr.ppingpong.domain.user.UserRepository;
import co.kr.ppingpong.dto.ResponseDto;
import co.kr.ppingpong.oauth.dto.kakao.KakaoLoginDto;
import co.kr.ppingpong.oauth.dto.kakao.KakaoTokenRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static co.kr.ppingpong.dto.user.UserRespDto.*;

@RequiredArgsConstructor
@RestController
public class Oauth2Controller {

    private final KakaoOauth2Service kakaoOauth2Service;

    // 프론트에서 인가코드를 받음 -> 인가코드와함께 post 요청을 보내줄 컨트롤러
    @GetMapping("/login/{oauthServerType}")
    ResponseEntity<?> login(@PathVariable String oauthServerType, @RequestParam String code) {

        KakaoTokenRespDto kakaoTokenRespDto = kakaoOauth2Service.액세스토큰받기(code);
        LoginUser loginUser = kakaoOauth2Service.사용자정보가져오기(kakaoTokenRespDto);
        LoginRespDto loginRespDto = kakaoOauth2Service.로그인(loginUser);

        return new ResponseEntity<>(new ResponseDto<>(1, "로그인 성공", loginRespDto), HttpStatus.OK);
    }
}
