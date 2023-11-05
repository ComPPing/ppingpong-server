package co.kr.ppingpong.domain.chat;

import co.kr.ppingpong.domain.BaseTimeEntity;
import co.kr.ppingpong.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Entity
@Table(name = "chatroom_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();

    @Builder
    public ChatRoom(Long id, User user, List<Message> messages) {
        this.id = id;
        this.user = user;
        this.messages = messages;
    }
}
