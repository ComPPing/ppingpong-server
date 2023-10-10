package co.kr.ppingpong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HealthCheckController {

    @GetMapping
    public String hi() {
        return "FTP 파일전송 성공했습니다.";
    }


}
