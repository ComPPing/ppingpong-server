package co.kr.ppingpong;

import co.kr.ppingpong.domain.user.User;
import co.kr.ppingpong.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class LoginTestController {

    private final UserRepository userRepository;

    @GetMapping("/check")
    public String check() {
        return "login";
    }

    @ResponseBody
    @GetMapping("/test")
    public String test() {
//        System.out.println("@Value " + KAKAO_CLIENT_ID);
        Optional<User> userOp = userRepository.findById(1L);
        if (userOp.isPresent()) {
            return "있다.";
        } else return "없다.";
    }

}
