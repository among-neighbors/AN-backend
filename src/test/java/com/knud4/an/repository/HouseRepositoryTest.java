package com.knud4.an.repository;

import com.knud4.an.exception.NotFoundException;
import com.knud4.an.house.entity.House;
import com.knud4.an.house.repository.HouseRepository;
import com.knud4.an.line.entity.Line;
import com.knud4.an.line.repository.LineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class HouseRepositoryTest {

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private LineRepository lineRepository;

    Line line;
    House house;

    @BeforeEach
    public void initDB() {
        this.line = Line.builder()
                .name("103")
                .build();

        lineRepository.save(line);

        Line findLine = lineRepository.findByName("103")
                .orElseThrow(() -> new NotFoundException("라인을 찾을 수 없습니다."));

        this.house = House.builder()
                .name("2000")
                .line(findLine)
                .build();




        houseRepository.save(house);
    }

    @Test
    public void findLineHouse() {
        IntStream.range(1, 6).forEach(i -> {
            House createdHouse = House.builder()
                    .line(this.line)
                    .name("200"+i).build();


            houseRepository.save(createdHouse);
        });

        Line findLine = lineRepository.findByName("103")
                .orElseThrow(() -> new NotFoundException("라인을 찾을 수 없습니다."));

        List<House> findHouses = houseRepository.findHousesByLineId(findLine.getId());

        for (House house:
             findHouses) {
            System.out.println(house.getName());
        }

        assertThat(findHouses.size()).isEqualTo(6);
    }
}
