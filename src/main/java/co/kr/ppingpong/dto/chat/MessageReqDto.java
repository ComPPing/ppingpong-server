package co.kr.ppingpong.dto.chat;

import co.kr.ppingpong.domain.chat.ChatRoom;
import co.kr.ppingpong.domain.chat.Message;
import lombok.Getter;
import lombok.Setter;

public class MessageReqDto {

    @Getter
    @Setter
    public static class MessageCreateReqDto {
        private Long chatRoomId;
        private String message;

        public Message toEntity(ChatRoom chatRoom) {
            return Message.builder()
                    .userText(message)
                    .chatRoom(chatRoom)
                    .build();
        }
    }
}
