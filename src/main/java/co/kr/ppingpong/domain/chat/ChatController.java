package co.kr.ppingpong.domain.chat;

import co.kr.ppingpong.config.auth.LoginUser;
import co.kr.ppingpong.dto.ResponseDto;
import co.kr.ppingpong.dto.chat.ChatRoomDto.ChatRoomCreateRespDto;
import co.kr.ppingpong.handler.ex.CustomApiException;
import co.kr.ppingpong.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/s")
@RequiredArgsConstructor
@RestController
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/chatRoom/{userId}")
    public ResponseEntity<?> createChatRoom(@PathVariable Long userId,
                                            @AuthenticationPrincipal LoginUser loginUser) {
        if (userId.longValue() != loginUser.getUser().getId().longValue()) throw new CustomApiException("다른 사용자의 채팅방을 만들 수 없습니다.");
        ChatRoomCreateRespDto chatRoomCreateRespDto = chatService.채팅방생성(userId);
        return new ResponseEntity<>(new ResponseDto<>(1, "채팅방 생성 완료", chatRoomCreateRespDto), HttpStatus.CREATED);
    }
}
