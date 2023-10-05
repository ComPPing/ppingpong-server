package co.kr.ppingpong.dto.user;

import co.kr.ppingpong.domain.user.User;
import co.kr.ppingpong.util.CustomDateUtil;
import lombok.Getter;
import lombok.Setter;

public class UserRespDto {

    @Getter
    @Setter
    public static class LoginRespDto {
        private Long id;
        private String name;
        private String createdAt;
        private String accessToken;

        public LoginRespDto(User user, String accessToken) {
            this.id = user.getId();
            this.name = user.getName();
            this.createdAt = CustomDateUtil.toStringFormat(user.getCreateDate());
            this.accessToken = accessToken;
        }
    }
}
