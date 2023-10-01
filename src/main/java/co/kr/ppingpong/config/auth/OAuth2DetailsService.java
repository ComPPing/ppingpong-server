package co.kr.ppingpong.config.auth;

import co.kr.ppingpong.domain.user.User;
import co.kr.ppingpong.domain.user.UserRepository;
import co.kr.ppingpong.handler.ex.CustomApiException;
import com.google.api.services.people.v1.PeopleServiceScopes;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OAuth2DetailsService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.debug("oauth2.getAttributes = " + oAuth2User.getAttributes());

        OAuth2Divider userInfo = null;
        String registrationId = userRequest.getClientRegistration().getRegistrationId();


        log.debug("OAuth2 서비스 id(구글,카카오,네이버) : " + registrationId);
        if (registrationId.equals("google")) {
            userInfo = new GoogleOAuth2(oAuth2User.getAttributes());
        } else if (registrationId.equals("kakao")) {
            userInfo = new KakaoOAuth2(oAuth2User.getAttributes());
        } else {
            throw new CustomApiException("OAuth2 로그인 실패");
        }

        String name = userInfo.getName();
        String email = userInfo.getEmail();

        Optional<User> userOP = userRepository.findByEmail(email);

        if (userOP.isPresent()) {
            LoginUser loginUser = new LoginUser(userOP.get(), oAuth2User.getAttributes());
            return loginUser;
        } else {
            User user = User.builder()
                    .name(name)
                    .email(email)
                    .build();
            User userPS = userRepository.save(user);
            LoginUser loginUser = new LoginUser(userPS, oAuth2User.getAttributes());
            return loginUser;
        }

    }


}
