package co.kr.ppingpong.config.auth;


import co.kr.ppingpong.domain.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

//    @Value("${jwt.secret}")
//    private String SECRET;
//
//    @Value("${jwt.token_prefix:null}")
//    private String TOKEN_PREFIX;

    // 토큰 생성
    public String accessTokenCreate(LoginUser loginUser) {
        String jwtToken = JWT.create()
                .withSubject("accessToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60 * 60)) //  1시간
//                .withExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60)) //  테스트용 : 1분
                .withClaim("id", loginUser.getUser().getId())
                .withClaim("email", loginUser.getUser().getEmail())
                .sign(Algorithm.HMAC512("ComppingSecret"));
        return "Bearer " + jwtToken;
    }

    // 토큰 검증 (return 되는 LoginUser 객체를 강제로 시큐리티 세션에 직접 주입할 예정)
    public LoginUser accessTokenVerify(String token) {

        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512("ComppingSecret")).build().verify(token);
        Long id = decodedJWT.getClaim("id").asLong();
        User user = User.builder().id(id).build();
        LoginUser loginUser = new LoginUser(user);
        return loginUser;
    }
}
