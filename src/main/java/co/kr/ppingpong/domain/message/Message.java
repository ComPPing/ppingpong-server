package co.kr.ppingpong.domain.message;

import co.kr.ppingpong.domain.BaseTimeEntity;
import co.kr.ppingpong.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "message_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;
    private String content;

    @ElementCollection
    private List<String> urls;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public Message(Long id, String role, String content, User user, List<String> urls) {
        this.id = id;
        this.role = role;
        this.urls = urls;
        this.content = content;
        this.user = user;
    }
}
