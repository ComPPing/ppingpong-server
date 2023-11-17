package co.kr.ppingpong.dto.message;

import co.kr.ppingpong.domain.message.Message;
import co.kr.ppingpong.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MessageReqDto {

    @Getter
    @Setter
    public static class MessageCreateReqDto {
        @Schema(example = "분위기 좋은 술집을 추천해줘")
        private String content;

        @Schema(example = "2")
        private Long id;

        public Message toEntity(User user) {

            return Message.builder()
                    .content(content)
                    .role("user")
                    .user(user)
                    .build();

        }

    }

    @Getter
    @Setter
    public static class TestFlaskApi {
        @Schema(example = "깔끔한 한식집 추천해줘")
        private String content;
        @Schema(example = "2")
        private Long id;

        public Message toEntity(User user) {

            return Message.builder()
                    .content(content)
                    .role("user")
                    .user(user)
                    .build();

        }

    }

    @Getter
    @Setter
    public static class PpingpongReqDto {

        List<DialogueDto> dialogue = new ArrayList<>();

        public PpingpongReqDto(List<Message> messageList) {
            this.dialogue = messageList.stream().map(DialogueDto::new).collect(Collectors.toList());
        }

        @Getter
        @Setter
        public static class DialogueDto {
            private String role;
            private String content;

            public DialogueDto(Message message) {
                this.role = message.getRole();
                this.content = message.getContent();
            }
        }
    }
}
