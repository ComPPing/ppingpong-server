package co.kr.ppingpong.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GenderEnum {
    FEMALE("여성"), MALE("남성");
    private String value;
}
