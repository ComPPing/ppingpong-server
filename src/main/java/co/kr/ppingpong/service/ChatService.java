package co.kr.ppingpong.service;

import co.kr.ppingpong.domain.chat.ChatRoom;
import co.kr.ppingpong.domain.chat.ChatRoomRepository;
import co.kr.ppingpong.domain.chat.Message;
import co.kr.ppingpong.domain.chat.MessageRepository;
import co.kr.ppingpong.domain.user.User;
import co.kr.ppingpong.domain.user.UserRepository;
import co.kr.ppingpong.dto.chat.ChatRoomDto.ChatRoomCreateRespDto;
import co.kr.ppingpong.dto.chat.MessageReqDto;
import co.kr.ppingpong.dto.chat.MessageReqDto.MessageCreateReqDto;
import co.kr.ppingpong.dto.chat.MessageRespDto;
import co.kr.ppingpong.dto.chat.MessageRespDto.MessageCreateRespDto;
import co.kr.ppingpong.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public ChatRoomCreateRespDto 채팅방생성(Long userId) {
        Optional<User> userOP = userRepository.findById(userId);
        if (userOP.isEmpty()) throw new CustomApiException("유저가 존재하지않습니다.");

        ChatRoom chatRoom = ChatRoom.builder()
                .user(userOP.get())
                .build();
        ChatRoom chatRoomPS = chatRoomRepository.save(chatRoom);

        return new ChatRoomCreateRespDto(chatRoomPS.getId(), userId);
    }

    public MessageCreateRespDto 메시지생성(MessageCreateReqDto messageCreateReqDto) {
        // 유저의 메시지 전송
        Optional<ChatRoom> chatRoomOP = chatRoomRepository.findById(messageCreateReqDto.getChatRoomId());
        if (chatRoomOP.isEmpty()) throw new CustomApiException("채팅방이 존재하지 않습니다.");
        Message messagePS = messageCreateReqDto.toEntity(chatRoomOP.get());
        Message message = messageRepository.save(messagePS);

        // 메시지를 gpt에게 전송해서 답변을 받음(대화 or 추천)
        // String gptMessage = callGpt(chatRoomOP.get())
        // message.setGptText(gptMessage);

//        return new MessageCreateRespDto(chatRoomOP.get().getId(), messagePS.getUserText(), gptText)

    }


    // public String callGpt(ChatRoom chatroom) {
    //   List<Message> messages = chatRoom.getMessages();
            // 모든 메시지를 gpt에다가 전송
    // return gptMessage;
    // }
}
