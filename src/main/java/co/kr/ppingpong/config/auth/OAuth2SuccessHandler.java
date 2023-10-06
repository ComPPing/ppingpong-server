package co.kr.ppingpong.config.auth;

import co.kr.ppingpong.dto.user.UserRespDto;
import co.kr.ppingpong.dto.user.UserRespDto.LoginRespDto;
import co.kr.ppingpong.util.CustomResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
//
//    @Value("${jwt.access_header}")
//    private String ACCESS_HEADER; //

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String accessToken = jwtProvider.accessTokenCreate(loginUser);
        response.addHeader("ACCESS_HEADER", accessToken);
        LoginRespDto loginRespDto = new LoginRespDto(loginUser.getUser(), accessToken);

        CustomResponseUtil.success(response, loginRespDto);
        response.sendRedirect("/");
    }

}
