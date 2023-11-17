package co.kr.ppingpong.service;

import co.kr.ppingpong.domain.message.Message;
import co.kr.ppingpong.domain.message.MessageRepository;
import co.kr.ppingpong.domain.restaurant.RestaurantRepository;
import co.kr.ppingpong.domain.user.User;
import co.kr.ppingpong.domain.user.UserRepository;
import co.kr.ppingpong.dto.message.MessageReqDto;
import co.kr.ppingpong.dto.message.MessageReqDto.MessageCreateReqDto;
import co.kr.ppingpong.dto.message.MessageReqDto.PpingpongReqDto;
import co.kr.ppingpong.dto.message.MessageReqDto.TestFlaskApi;
import co.kr.ppingpong.dto.message.MessageRespDto;
import co.kr.ppingpong.dto.message.MessageRespDto.MessageCreateRespDto;
import co.kr.ppingpong.dto.message.MessageRespDto.MessagesRespDto;
import co.kr.ppingpong.dto.message.MessageRespDto.PpingpongRespDto;
import co.kr.ppingpong.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class MessageService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final RestaurantRepository restaurantRepository;

    public MessagesRespDto 메시지내역(Long userId) {
        User userPS = userRepository.findById(userId).orElseThrow(() -> new CustomApiException("해당 id 에 해당하는 유저가 없습니다."));
        List<Message> messages = messageRepository.findByUser(userPS);

        return new MessagesRespDto(messages);
    }

    public ResponseEntity<?> flaskApi테스트(TestFlaskApi testFlaskApi) {

        User userPS = userRepository.findById(testFlaskApi.getId()).orElseThrow(() -> new CustomApiException("해당 id 에 해당하는 유저가 없습니다."));

        // 유저의 메시지 전송
        Message message = testFlaskApi.toEntity(userPS);
        messageRepository.save(message);
        List<Message> messageList = messageRepository.findByUser(userPS);

        // 메시지를 gpt에게 전송해서 답변을 받음(대화 or 추천)
        ResponseEntity<?> response = testCallAssistant(messageList);

        return response;
    }

    public ResponseEntity<?> testCallAssistant(List<Message> messageList) {
        PpingpongReqDto dialogue = new PpingpongReqDto(messageList);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<?> body = new HttpEntity<>(dialogue);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:1024/conversation", HttpMethod.POST, body, String.class);
        return response;
    }

    public MessageCreateRespDto 메시지생성(MessageCreateReqDto messageCreateReqDto) {

//        User userPS = userRepository.findById(userId).orElseThrow(() -> new CustomApiException("해당 id 에 해당하는 유저가 없습니다."));
        User userPS = userRepository.findById(messageCreateReqDto.getId()).orElseThrow(() -> new CustomApiException("해당 id 에 해당하는 유저가 없습니다."));

        // 유저의 메시지 전송
        Message message = messageCreateReqDto.toEntity(userPS);
        messageRepository.save(message);
        List<Message> messageList = messageRepository.findByUser(userPS);

        // 메시지를 gpt에게 전송해서 답변을 받음(대화 or 추천)
        PpingpongRespDto ppingpongRespDto = callAssistant(messageList);

        if (ppingpongRespDto.getMessage_type().equals("Conversation")) {
            // assistant 메시지 저장
            messageRepository.save(ppingpongRespDto.toEntity(userPS));
            return new MessageCreateRespDto(ppingpongRespDto.getMessage());
        } else {
            // Recommendation
            // dto : 간단한 설명, 식당설명(분류), url(네이버 플레이스 id 끼워넣기)

            return new MessageCreateRespDto(ppingpongRespDto.getMessage()); // 변경 필요
        }


    }

     public PpingpongRespDto callAssistant(List<Message> messageList) {
         PpingpongReqDto dialogue = new PpingpongReqDto(messageList);
         RestTemplate restTemplate = new RestTemplate();
         PpingpongRespDto ppingpongRespDto = restTemplate.postForObject("http://localhost:1024/conversation", dialogue, PpingpongRespDto.class);

        return ppingpongRespDto;
     }
}
