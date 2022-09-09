package com.knud4.an.line.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineDTO {
    private Long lineId;
    private String lineName;
}
