package co.kr.ppingpong.oauth.dto.kakao;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class KakaoTokenRespDto {
    private String token_type;
    private String access_token;
    private Integer expires_in;
    private String refresh_token;
    private Integer refresh_token_expires_in;
}
