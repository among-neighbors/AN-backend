package com.knud4.an.websocket.controller;

import com.knud4.an.account.entity.Account;
import com.knud4.an.account.repository.AccountRepository;
import com.knud4.an.exception.NotFoundException;
import com.knud4.an.house.entity.House;
import com.knud4.an.house.repository.HouseRepository;
import com.knud4.an.line.entity.Line;
import com.knud4.an.line.repository.LineRepository;
import com.knud4.an.security.provider.JwtProvider;
import com.knud4.an.websocket.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AlertController {
    private final SimpMessagingTemplate template;
    private final JwtProvider jwtProvider;

    private final AccountRepository accountRepository;
    private final LineRepository lineRepository;
    private final HouseRepository houseRepository;

    @MessageMapping(value = "/alert")
    public void alert(MessageDTO<?> messageDTO) {
        String email = jwtProvider.getEmailFromToken(messageDTO.getToken());

        Account account = accountRepository.findAccountByEmail(email)
                .orElseThrow(() -> new NotFoundException("계정을 찾을 수 없습니다."));
        Line line = lineRepository.findById(account.getLine().getId());
        House house = houseRepository.findById(account.getHouse().getId());

        template.convertAndSend("/sub/line/" + line.getName()
                , house.getName() + "호 에서 긴급요청이 왔습니다. 메세지: " + messageDTO.getText());
    }
}
