package co.kr.ppingpong.oauth;

import co.kr.ppingpong.domain.user.User;
import co.kr.ppingpong.domain.user.UserRepository;
import co.kr.ppingpong.dto.ResponseDto;
import co.kr.ppingpong.dto.user.UserRespDto;
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
    private final UserRepository userRepository;

    // e.g. https://ppingpong.store/login/oauth2/code/kakao?code=
    // 프론트에서 인가코드를 받음 -> 인가코드와함께 post 요청을 보내줄 컨트롤러
    @GetMapping("/login/oauth2/code/{oauthServerType}")
    ResponseEntity<?> login(@PathVariable String oauthServerType, @RequestParam String code) {
        Optional<User> userOp = userRepository.findById(1L);

        String accessToken = kakaoOauth2Service.액세스토큰받기(code).toString();
        LoginRespDto loginRespDto = kakaoOauth2Service.로그인(accessToken);
        return new ResponseEntity<>(new ResponseDto<>(1, "로그인 성공", loginRespDto), HttpStatus.OK);
    }
}
