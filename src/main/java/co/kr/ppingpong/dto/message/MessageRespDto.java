package co.kr.ppingpong.dto.message;

import co.kr.ppingpong.domain.message.Message;
import co.kr.ppingpong.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MessageRespDto {
    @Getter
    @Setter
    public static class MessageCreateRespDto {
        private String content;

        public MessageCreateRespDto(String content) {
            this.content = content;
        }
    }

    @Getter
    @Setter
    public static class MessagesRespDto {
        List<MyMessageDto> myMessageDtoList = new ArrayList<>();

        public MessagesRespDto(List<Message> messages) {
            this.myMessageDtoList = messages.stream().map(MyMessageDto::new).collect(Collectors.toList());
        }

        @Getter
        @Setter
        public static class MyMessageDto {
            private String role;
            private String content;
            private List<String> urls;

            public MyMessageDto(Message message) {
                this.role = message.getRole();
                this.content = message.getContent();
                this.urls = message.getUrls();
            }
        }
    }

    @Getter
    @Setter
    public static class PpingpongRespDto {
        // urls이 어떻게 도착하는지 알아야됨
        private boolean success;
        private String message;
        private String error;
        // Conversation, Recommendation
        private String message_type;

        public Message toEntity(User user) {

            return Message.builder()
                    .content(message)
                    .role("assistant")
                    .user(user)
                    .build();

        }
    }

    @Getter
    @Setter
    public static class MessageRecommendDto {

    }
}
