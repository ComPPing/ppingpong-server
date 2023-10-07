package co.kr.ppingpong.oauth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class KakaoAccountDto {

    private KakaoAccount kakao_account;

    @Getter
    @Setter
    public static class KakaoAccount {

        private Profile profile;
        private String email;

    }

    @Getter
    @Setter
    public static class Profile {
        private String nickname;
    }
}
