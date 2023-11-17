package co.kr.ppingpong.domain.message;

import co.kr.ppingpong.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByUser(User user);

}
