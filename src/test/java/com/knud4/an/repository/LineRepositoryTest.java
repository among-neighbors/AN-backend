package com.knud4.an.repository;

import com.knud4.an.exceptions.NotFoundException;
import com.knud4.an.line.entity.Line;
import com.knud4.an.line.repository.LineRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.*;

import java.util.stream.IntStream;

@SpringBootTest
@Transactional
public class LineRepositoryTest {

    @Autowired
    private LineRepository lineRepository;

    @Test
    public void saveTest() {
        Line line = Line.builder().name("101").build();
        lineRepository.save(line);

        Line findLine = lineRepository.findByName("101")
                .orElseThrow(() -> new NotFoundException("라인을 찾을 수 없습니다."));

        assertThat(line).isEqualTo(findLine);
    }
}
