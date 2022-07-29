package com.knud4.an.line.service;

import com.knud4.an.line.dto.CreateLineDTO;
import com.knud4.an.line.dto.FindLineDTO;
import com.knud4.an.line.dto.UpdateLineDTO;
import com.knud4.an.line.entity.Line;
import com.knud4.an.line.repository.LineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;

    @Transactional
    public void createLine(CreateLineDTO createLineDTO) {
//        Manager 체크 로직 추가

        Line line = Line.builder()
                .name(createLineDTO.getName())
                .build();

        lineRepository.save(line);
    }

    public FindLineDTO findByLineName(String findName) {
        Line findLine = lineRepository.findByName(findName);

        FindLineDTO findLineDTO = new FindLineDTO();
        findLineDTO.setName(findLine.getName());

        return findLineDTO;
    }

    public List<FindLineDTO> findAllLines() {
        List<Line> lines = lineRepository.findAll();
        List<FindLineDTO> findLineDTOS = new ArrayList<>();

        for (Line line : lines) {
            FindLineDTO findLineDTO = new FindLineDTO();
            findLineDTO.setName(line.getName());

            findLineDTOS.add(findLineDTO);
        }

        return findLineDTOS;
    }

    @Transactional
    public void updateLineName(UpdateLineDTO updateLineDTO) {
        //        Manager 체크 로직 추가
        lineRepository.updateName(updateLineDTO.oldName, updateLineDTO.changeName);
    }
}