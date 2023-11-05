package co.kr.ppingpong.dto.chat;

import lombok.Getter;
import lombok.Setter;

public class ChatRoomDto {

    @Getter
    @Setter
    public static class ChatRoomCreateRespDto {
        private Long id;
        private Long userId;

        public ChatRoomCreateRespDto(Long id, Long userId) {
            this.id = id;
            this.userId = userId;
        }
    }
}
