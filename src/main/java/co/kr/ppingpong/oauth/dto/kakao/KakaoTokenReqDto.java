package co.kr.ppingpong.oauth.dto.kakao;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KakaoTokenReqDto {
    private String client_id;
    private String redirect_uri;
    private String grant_type;
    private String code;


    @Builder
    public KakaoTokenReqDto(String client_id, String redirect_uri, String grant_type, String code) {
        this.client_id = client_id;
        this.redirect_uri = redirect_uri;
        this.grant_type = grant_type;
        this.code = code;
    }
}
