package co.kr.ppingpong.oauth.dto.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoLoginDto {
    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    public class KakaoAccount {
        private String email;
        @JsonProperty("profile")
        public KakaoProfile kakaoProfile;

        @Getter
        public class KakaoProfile {
            public String nickname;
        }
    }



    public String getEmail() {
        return kakaoAccount.email;
    }



}
