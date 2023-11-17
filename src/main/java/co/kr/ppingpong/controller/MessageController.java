package co.kr.ppingpong.controller;

import co.kr.ppingpong.config.auth.LoginUser;
import co.kr.ppingpong.dto.ResponseDto;
import co.kr.ppingpong.dto.message.MessageReqDto;
import co.kr.ppingpong.dto.message.MessageReqDto.MessageCreateReqDto;
import co.kr.ppingpong.dto.message.MessageReqDto.TestFlaskApi;
import co.kr.ppingpong.dto.message.MessageRespDto;
import co.kr.ppingpong.dto.message.MessageRespDto.MessageCreateRespDto;
import co.kr.ppingpong.dto.message.MessageRespDto.MessagesRespDto;
import co.kr.ppingpong.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api/ss/messages")
@RestController
public class MessageController {

    private final MessageService messageService;


    @Operation(summary = "메시지 불러오기")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "전체 메시지 조회 성공", content = @Content(schema = @Schema(implementation = MessagesRespDto.class))),
            @ApiResponse(responseCode = "400", description = "해당 id 에 해당하는 유저가 없습니다.")
    })
    @GetMapping
    public ResponseEntity<?> getMessage(@AuthenticationPrincipal @Parameter(hidden = true)
                                            LoginUser loginUser) {

        MessagesRespDto messagesRespDto = messageService.메시지내역(loginUser.getUser().getId());

        return new ResponseEntity<>(new ResponseDto<>(1,"전체 메시지 조회 성공", messagesRespDto), HttpStatus.OK);
    }

    @Operation(summary = "메시지 전송", description = "바디에 {userText} 을 json 형식으로 보내주세요")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "메시지 전송 성공", content = @Content(schema = @Schema(implementation = MessagesRespDto.class))),
            @ApiResponse(responseCode = "400", description = "해당 id 에 해당하는 유저가 없습니다.")
    })
    @PostMapping
    public ResponseEntity<?> saveMessage(@RequestBody @Valid MessageCreateReqDto messageCreateReqDto, BindingResult bindingResult,
                                         @Parameter(hidden = true)
                                         @AuthenticationPrincipal LoginUser loginUser) {

//        MessageCreateRespDto messageCreateRespDto = messageService.메시지생성(messageCreateReqDto, loginUser.getUser().getId());
        MessageCreateRespDto messageCreateRespDto = messageService.메시지생성(messageCreateReqDto);

        return new ResponseEntity<>(new ResponseDto<>(1,"메시지 전송 성공", messageCreateRespDto), HttpStatus.CREATED);
    }

    @PostMapping("/test")
    public ResponseEntity<?> testFlaskApi(@RequestBody @Valid TestFlaskApi testFlaskApi, BindingResult bindingResult) {

        ResponseEntity<?> response = messageService.flaskApi테스트(testFlaskApi);

//        return new ResponseEntity<>(new ResponseDto<>(1,"메시지 전송 성공", messageCreateRespDto), HttpStatus.CREATED);
        return response;
    }
}
