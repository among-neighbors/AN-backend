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
import java.util.List;
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


        String houseInfo = line + house;
        redisUtil.set(houseInfo, true);
        redisUtil.expire(houseInfo, 600);

        List<String> locationInfo = List.of(messageDTO.getLat(), messageDTO.getLng());
        redisUtil.rPush(MessageDTO.LOCATION_PREFIX + houseInfo, locationInfo);
        redisUtil.expire(MessageDTO.LOCATION_PREFIX, 600);

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

        String houseInfo = line + acceptDTO.getTarget();

        Boolean isPresent = (Boolean) redisUtil.get(houseInfo);
        if (isPresent == null || !isPresent) {
            throw new IllegalMessagingException("긴급 요청 목록에 없습니다.");
        }

        List<Object> locationInfoList = redisUtil.lRange(MessageDTO.LOCATION_PREFIX + houseInfo, 0, 1);
        List<String> locationInfo = (List<String>)locationInfoList.get(0);

        JSONObject result = new JSONObject();
        result.put("accept_house", house);
        result.put("target_house", acceptDTO.getTarget());
        result.put("lat", locationInfo.get(0));
        result.put("lng", locationInfo.get(1));

        Map<String, Object> header = new HashMap<>();
        header.put("type", "accept");

        template.convertAndSend("/sub/line/" + line, result.toString(), header);

        redisUtil.del(line+acceptDTO.getTarget());
    }
}
