package com.knud4.an.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO<T> {
    private T text;
    private String lat;
    private String lng;
}
