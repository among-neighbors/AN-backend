package com.knud4.an.websocket.controller;

import com.knud4.an.exception.websocket.IllegalMessagingException;
import com.knud4.an.websocket.dto.AcceptDTO;
import com.knud4.an.websocket.dto.MessageDTO;
import com.knud4.an.utils.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class AlertController {
    private final SimpMessagingTemplate template;
    private final RedisUtil redisUtil;

    @MessageMapping(value = "/alert")
    public void alert(MessageDTO<?> messageDTO, SimpMessageHeaderAccessor accessor) {
        String line;
        String house;

        try {
            line = accessor.getNativeHeader("line").get(0);
            house = accessor.getNativeHeader("house").get(0);
        } catch (Exception e) {
            throw new IllegalMessagingException("세대 정보 누락");
        }

//        Boolean isPresent = (Boolean)redisUtil.get(line+house);
//        if (isPresent != null && isPresent) {
//            throw new IllegalMessagingException("이미 요청 하였습니다.");
//        }

        JSONObject result = new JSONObject();

        result.put("house", house);
        result.put("text", messageDTO.getText());
        result.put("lat", messageDTO.getLat());
        result.put("lng", messageDTO.getLng());

        redisUtil.set(line+house, true);
        redisUtil.expire(line+house, 600);

        Map<String, Object> header = new HashMap<>();
        header.put("type", "alert");

        template.convertAndSend("/sub/line/" + line, result.toString(), header);
    }

    @MessageMapping(value = "/accept")
    public void accept(@Valid AcceptDTO acceptDTO, SimpMessageHeaderAccessor accessor) {
        String line;
        String house;

        try {
            line = accessor.getNativeHeader("line").get(0);
            house = accessor.getNativeHeader("house").get(0);
        } catch (Exception e) {
            throw new IllegalMessagingException("세대 정보 누락");
        }

        Boolean isPresent = (Boolean) redisUtil.get(line+acceptDTO.getTarget());
        if (isPresent == null || !isPresent) {
            throw new IllegalMessagingException("긴급 요청 목록에 없습니다.");
        }

        JSONObject result = new JSONObject();

        result.put("accept_house", house);
        result.put("target_house", acceptDTO.getTarget());

        Map<String, Object> header = new HashMap<>();
        header.put("type", "accept");

        template.convertAndSend("/sub/line/" + line, result.toString(), header);

        redisUtil.del(line+acceptDTO.getTarget());
    }
}
