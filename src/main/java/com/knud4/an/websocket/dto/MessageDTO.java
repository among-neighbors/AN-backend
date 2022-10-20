package com.knud4.an.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO<T> {
    public static final String LOCATION_PREFIX = "LC::";

    private T text;
    private String lat;
    private String lng;
}
