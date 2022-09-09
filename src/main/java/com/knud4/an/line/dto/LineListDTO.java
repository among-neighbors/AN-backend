package com.knud4.an.line.dto;

import com.knud4.an.line.entity.Line;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class LineListDTO {
    private final List<LineDTO> lines;

    public LineListDTO(List<Line> lineList) {
        this.lines = lineList.stream()
                .map(l -> new LineDTO(l.getId(), l.getName()))
                .collect(Collectors.toList());
    }
}
