package com.knud4.an.service;

import com.knud4.an.line.dto.CreateLineDTO;
import com.knud4.an.line.dto.FindLineDTO;
import com.knud4.an.line.dto.UpdateLineDTO;
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
        CreateLineDTO dto = new CreateLineDTO();
        dto.setName("101동");

        lineService.createLine(dto);

        FindLineDTO findLineDTO = new FindLineDTO("101동");

        FindLineDTO findDTO = lineService.findByLineName("101동");

        assertEquals(findDTO.getName(), "101동");
    }

    @Test
    public void findLines() {
        IntStream.range(1, 11).forEach(i -> {
            CreateLineDTO dto = new CreateLineDTO();
            dto.setName("10"+i);

            lineService.createLine(dto);
        });

        List<FindLineDTO> DTOs = lineService.findAllLines();

        assertEquals(DTOs.size(), 10);

        for (FindLineDTO dto:
             DTOs) {
            System.out.println(dto.getName());
        }
    }

    @Test
    public void updateLineName() {
        CreateLineDTO dto = new CreateLineDTO();
        dto.setName("101동");

        lineService.createLine(dto);

        UpdateLineDTO updateLineDTO = new UpdateLineDTO();
        updateLineDTO.setOldName("101동");
        updateLineDTO.setChangeName("103동");

        lineService.updateLineName(updateLineDTO);

        FindLineDTO findLine = lineService.findByLineName("103동");
    }
}
