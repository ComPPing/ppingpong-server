package co.kr.ppingpong.dto.chat;

import lombok.Getter;
import lombok.Setter;

public class MessageRespDto {
    @Getter
    @Setter
    public static class MessageCreateRespDto {
        private Long chatRoomId;
        private String userText;
        private String gptText;

        public MessageCreateRespDto(Long chatRoomId, String userText, String gptText) {
            this.chatRoomId = chatRoomId;
            this.userText = userText;
            this.gptText = gptText;
        }
    }
}
