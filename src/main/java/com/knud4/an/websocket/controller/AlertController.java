package com.knud4.an.websocket.controller;

import com.knud4.an.websocket.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AlertController {
    private final SimpMessagingTemplate template;

    @MessageMapping(value = "/alert")
    public void alert(MessageDTO<?> messageDTO, SimpMessageHeaderAccessor accessor) {
        String line = accessor.getNativeHeader("line").get(0);
        String house = accessor.getNativeHeader("house").get(0);

        template.convertAndSend("/sub/line/" + line,
                house + "호 에서 긴급요청이 왔습니다. 메세지: " + messageDTO.getText());
    }
}
