package co.kr.ppingpong.domain.chat;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "message_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ChatRoom chatRoom;

    private String userText;
    private String gptText;

    public void setGptText(String gptText) {
        this.gptText = gptText;
    }

    @Builder
    public Message(Long id, ChatRoom chatRoom, String userText, String gptText) {
        this.id = id;
        this.chatRoom = chatRoom;
        this.userText = userText;
        this.gptText = gptText;
    }
}
