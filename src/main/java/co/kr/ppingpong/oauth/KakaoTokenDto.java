package co.kr.ppingpong.oauth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class KakaoTokenDto {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private String id_token;
    private int expires_in;
    private int refresh_token_expires_in;

}
