package co.kr.ppingpong.domain.review;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "review_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    private Long id;

    @Lob
    private String text;
}
