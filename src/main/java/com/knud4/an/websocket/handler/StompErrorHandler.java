package com.knud4.an.websocket.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

public class StompErrorHandler extends StompSubProtocolErrorHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        logger.error(ex.getCause().getMessage(), ex);

        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
        accessor.setMessage("connection error");
        accessor.setLeaveMutable(true);

        return MessageBuilder.createMessage(ex.getCause().getMessage().getBytes(), accessor.getMessageHeaders());
    }
}
