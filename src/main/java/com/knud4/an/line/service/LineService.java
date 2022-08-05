package com.knud4.an.line.service;

import com.knud4.an.exceptions.NotFoundException;
import com.knud4.an.line.dto.CreateLineForm;
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
    public Long createLine(CreateLineForm createLineForm) {
//        Manager 체크 로직 추가

        Line line = Line.builder()
                .name(createLineForm.getName())
                .build();

        lineRepository.save(line);

        return line.getId();
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id);
    }

    public Line findLineByName(String findName) {
        return lineRepository.findByName(findName)
                .orElseThrow(() -> new NotFoundException("라인을 찾을 수 없습니다."));
    }

    public List<Line> findAllLines() {
        return lineRepository.findAll();
    }
}