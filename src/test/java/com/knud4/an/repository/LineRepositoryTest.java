package com.knud4.an.repository;

import com.knud4.an.line.entity.Line;
import com.knud4.an.line.repository.LineRepository;
import com.knud4.an.Member.Entity.GeneralMember;
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

        Line findLine = lineRepository.findByName("101");

        assertThat(line).isEqualTo(findLine);
    }

    @Test
    public void updateTest() {
        IntStream.range(1, 4).forEach(i -> {
            Line line = Line.builder().name("10" + i).build();
            lineRepository.save(line);
        });

        lineRepository.updateName("101", "105");

        try {
            lineRepository.findByName("101");
            fail("수정 실패");
        } catch (Exception e) {
        }
    }

    @Test
    public void deleteTest() {
        IntStream.range(1, 4).forEach(i -> {
            Line line = Line.builder().name("10" + i).build();
            lineRepository.save(line);
        });

        lineRepository.deleteByName("101");

        try {
            lineRepository.findByName("101");
            fail("삭제 실패");
        } catch (Exception e) {
        }
    }
}
