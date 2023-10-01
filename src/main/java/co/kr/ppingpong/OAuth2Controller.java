package co.kr.ppingpong;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OAuth2Controller {

    @GetMapping("/check")
    public String check() { // html 읽으려면 타임리프 필요
        return "login";
    }
}
