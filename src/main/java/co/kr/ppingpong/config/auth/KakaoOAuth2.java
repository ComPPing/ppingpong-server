package co.kr.ppingpong.config.auth;

import lombok.Getter;

import java.util.Map;

@Getter
public class KakaoOAuth2 implements OAuth2Divider{

    private Map<String, Object> attributes;
    private Map<String, Object> propertiesAttributes;
    private Map<String, Object> kakaoAccountAttributes;

    public KakaoOAuth2(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.propertiesAttributes = (Map<String, Object>) attributes.get("properties");
        this.kakaoAccountAttributes = (Map<String, Object>) attributes.get("kakao_account");
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getName() {
        return (String) propertiesAttributes.get("nickname");
    }

    @Override
    public String getEmail() {
        return (String) kakaoAccountAttributes.get("email");
    }
}
