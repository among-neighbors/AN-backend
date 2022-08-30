package com.knud4.an.websocket.interceptor;

import com.knud4.an.account.entity.Account;
import com.knud4.an.account.repository.AccountRepository;
import com.knud4.an.exception.NotFoundException;
import com.knud4.an.line.entity.Line;
import com.knud4.an.line.repository.LineRepository;
import com.knud4.an.security.provider.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StompInterceptor implements ChannelInterceptor {

    private final String SUB_LINE_PREFIX = "/sub/line/";

    private final JwtProvider jwtProvider;
    
    private final LineRepository lineRepository;
    private final AccountRepository accountRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
            Line line = getLineByAuthorizationHeader(headerAccessor);

            Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
            sessionAttributes.put("line", line.getName());
            headerAccessor.setSessionAttributes(sessionAttributes);

        } else if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            validateSubscriptionHeader(headerAccessor);
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

    private Line getLineByAuthorizationHeader(StompHeaderAccessor headerAccessor) {
        List<String> authorization = headerAccessor.getNativeHeader("Authorization");
        if (authorization == null || authorization.size() != 1) {
            throw new NotFoundException("인증 정보를 찾을 수 없습니다.");
        }

        String token = authorization.get(0);
        Long accountId = jwtProvider.getAccountIdFromToken(token);

        Account account = accountRepository.findAccountById(accountId);
        Line line = lineRepository.findById(account.getLine().getId());
        return line;
    }


}
