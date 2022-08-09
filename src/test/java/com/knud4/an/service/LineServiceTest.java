package com.knud4.an.service;

import com.knud4.an.line.dto.CreateLineForm;
import com.knud4.an.line.entity.Line;
import com.knud4.an.line.service.LineService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
@Transactional
public class LineServiceTest {

    @Autowired
    private LineService lineService;

    @Test
    public void createLine() {
        CreateLineForm dto = new CreateLineForm();
        dto.setName("101동");

        lineService.createLine(dto);

        Line findLine = lineService.findLineByName("101동");

        assertEquals(findLine.getName(), "101동");
    }

    @Test
    public void findLines() {
        IntStream.range(1, 11).forEach(i -> {
            CreateLineForm dto = new CreateLineForm();
            dto.setName("10"+i);

            lineService.createLine(dto);
        });

        List<Line> findLines = lineService.findAllLines();

        assertEquals(findLines.size(), 10);

        for (Line line:
             findLines) {
            System.out.println(line.getName());
        }
    }
}
