package com.knud4.an.websocket.interceptor;

import com.knud4.an.account.entity.Account;
import com.knud4.an.account.repository.AccountRepository;
import com.knud4.an.exception.NotFoundException;
import com.knud4.an.house.entity.House;
import com.knud4.an.house.repository.HouseRepository;
import com.knud4.an.line.entity.Line;
import com.knud4.an.line.repository.LineRepository;
import com.knud4.an.security.provider.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StompInboundInterceptor implements ChannelInterceptor {

    private final String SUB_LINE_PREFIX = "/sub/line/";

    private final JwtProvider jwtProvider;
    
    private final LineRepository lineRepository;
    private final HouseRepository houseRepository;
    private final AccountRepository accountRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();

        if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
            Map<String, Object> info = getHouseInfoByAuthorizationHeader(headerAccessor);

            sessionAttributes.put("line", ((Line)info.get("line")).getName());
            sessionAttributes.put("house", ((House)info.get("house")).getName());
            headerAccessor.setSessionAttributes(sessionAttributes);
        }

        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            validateSubscriptionHeader(headerAccessor);
        }

        if (StompCommand.SEND.equals(headerAccessor.getCommand())) {
            String destination = headerAccessor.getDestination();

            if (destination == null || !destination.startsWith("/pub")) {
                throw new IllegalStateException("잘못된 접근 입니다.");
            }

            headerAccessor.setNativeHeader("line", sessionAttributes.get("line").toString());
            headerAccessor.setNativeHeader("house", sessionAttributes.get("house").toString());

            message = MessageBuilder.createMessage(message.getPayload(), headerAccessor.toMessageHeaders());
        }

        return message;
    }

    private void validateSubscriptionHeader(StompHeaderAccessor headerAccessor) {
        String lineName = (String) headerAccessor.getSessionAttributes().get("line");
        String destination = headerAccessor.getDestination();

        if (destination != null && destination.startsWith(SUB_LINE_PREFIX)) {
            if (!destination.replace(SUB_LINE_PREFIX, "").equals(lineName)) {
                throw new IllegalStateException("인증 정보가 잘못되었습니다.");
            }
        }
    }

    private Map<String, Object> getHouseInfoByAuthorizationHeader(StompHeaderAccessor headerAccessor) {
        List<String> authorization = headerAccessor.getNativeHeader("Authorization");
        if (authorization == null || authorization.size() != 1) {
            throw new NotFoundException("인증 정보를 찾을 수 없습니다.");
        }

        String token = authorization.get(0);
        Long accountId = jwtProvider.getAccountIdFromToken(token);

        Account account = accountRepository.findAccountById(accountId);
        Line line = lineRepository.findById(account.getLine().getId());
        House house = houseRepository.findById(account.getHouse().getId());

        Map<String, Object> result = new HashMap<>();
        result.put("line", line);
        result.put("house", house);

        return result;
    }


}
