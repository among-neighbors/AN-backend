package com.knud4.an.websocket.interceptor;

import com.knud4.an.account.entity.Account;
import com.knud4.an.account.repository.AccountRepository;
import com.knud4.an.exception.NotFoundException;
import com.knud4.an.exception.websocket.IllegalMessagingException;
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
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 서버 스펙의 STOMP 메세징 채널 인터셉터
 * @see com.knud4.an.config.StompWebSocketConfig
 */
@Component
@RequiredArgsConstructor
public class StompInboundInterceptor implements ChannelInterceptor {

    private final String SUB_LINE_PREFIX = "/sub/line/";

    private final JwtProvider jwtProvider;
    
    private final LineRepository lineRepository;
    private final HouseRepository houseRepository;
    private final AccountRepository accountRepository;

    /**
     * COMMAND 별 필터링 로직 정의
     * @see StompHeaderAccessor
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();

        if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
            Map<String, Object> info = getHouseInfoByAuthorizationHeader(headerAccessor);

            Assert.notNull(sessionAttributes, "null session");

            sessionAttributes.put("line", ((Line)info.get("line")).getName());
            sessionAttributes.put("house", ((House)info.get("house")).getName());
            headerAccessor.setSessionAttributes(sessionAttributes);
        }

        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            Assert.notNull(headerAccessor.getDestination(), "null destination");

            if (!headerAccessor.getDestination().startsWith("/user")) {
                validateSubscriptionHeader(headerAccessor);
            }
        }

        if (StompCommand.SEND.equals(headerAccessor.getCommand())) {
            String destination = headerAccessor.getDestination();

            if (destination == null || !destination.startsWith("/pub")) {
                throw new IllegalStateException("잘못된 접근 입니다.");
            }

            Assert.notNull(sessionAttributes, "null session");

            headerAccessor.setNativeHeader("line", sessionAttributes.get("line").toString());
            headerAccessor.setNativeHeader("house", sessionAttributes.get("house").toString());
            headerAccessor.setLeaveMutable(true);

            message = MessageBuilder.createMessage(message.getPayload(), headerAccessor.getMessageHeaders());
        }

        return message;
    }

    /**
     * 구독 요청 메세징 헤더 검증
     * @exception IllegalStateException
     *            <p>요청 형식이 올바르지 않을 때</p>
     *            session 에 저장된 라인 정보와 구독 요청 라인 정보가 일치하지 않을 때
     */
    private void validateSubscriptionHeader(StompHeaderAccessor headerAccessor) {
        String lineName = (String) headerAccessor.getSessionAttributes().get("line");
        String destination = headerAccessor.getDestination();

        if (destination != null && destination.startsWith(SUB_LINE_PREFIX)) {
            if (!destination.replace(SUB_LINE_PREFIX, "").equals(lineName)) {
                throw new IllegalMessagingException("요청 라인 이름이 잘못되었습니다.");
            }
        } else {
            throw new IllegalMessagingException("요청 형식이 잘못되었습니다.");
        }
    }

    /**
     * 메세징 인증 헤더에 포함된 세대 정보 제공
     * @exception NotFoundException 요청 헤더가 올바르지 않을 때
     */
    private Map<String, Object> getHouseInfoByAuthorizationHeader(StompHeaderAccessor headerAccessor) {
        List<String> authorization = headerAccessor.getNativeHeader("Authorization");
        if (authorization == null || authorization.size() != 1) {
            throw new IllegalMessagingException("인증 정보를 찾을 수 없습니다.");
        }

        String token = authorization.get(0);
        Long accountId = jwtProvider.getAccountIdFromToken(token);

        Account account = accountRepository.findAccountById(accountId)
                .orElseThrow(() -> new IllegalMessagingException("계정이 존재하지 않습니다."));
        Line line = lineRepository.findById(account.getLine().getId());
        House house = houseRepository.findById(account.getHouse().getId());

        Map<String, Object> result = new HashMap<>();
        result.put("line", line);
        result.put("house", house);

        return result;
    }


}
