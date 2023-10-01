package co.kr.ppingpong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HealthCheckController {

    @GetMapping
    public String hi() {
        return "dev에서 merge 요청했습니다.";
    }


}
