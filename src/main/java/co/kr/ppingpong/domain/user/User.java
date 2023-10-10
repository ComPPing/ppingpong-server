package co.kr.ppingpong.domain.user;

import co.kr.ppingpong.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.persistence.*;

@Getter
@Entity
@Table(name = "user_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity { //

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 40, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @Column(length = 3)
    private int age;

    @Builder
    public User(Long id, String name, String email, GenderEnum gender, int age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.age = age;
    }
}
