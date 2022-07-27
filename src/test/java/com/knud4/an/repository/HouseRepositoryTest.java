package com.knud4.an.repository;

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

        this.house = House.builder()
                .name("2000")
                .build();

        Line findLine = lineRepository.findByName("103");

        house.setLine(findLine);

        houseRepository.save(house);
    }

    @Test
    public void update() {
        houseRepository.updateHouseName("103", "2000", "2001");
        try {
            houseRepository.findByName("103", "2000");
            fail("수정 실패");
        } catch (Exception e) {

        }
    }

    @Test
    public void findLineHouse() {
        IntStream.range(1, 6).forEach(i -> {
            House createdHouse = House.builder().name("200"+i).build();
            createdHouse.setLine(this.line);

            houseRepository.save(createdHouse);
        });

        List<House> findHouses = houseRepository.findHousesByLineName("103");

        for (House house:
             findHouses) {
            System.out.println(house.getName());
        }

        assertThat(findHouses.size()).isEqualTo(6);
    }

    @Test
    public void deleteHouse() {
        houseRepository.deleteByName("103", "2000");

        try {
            houseRepository.findByName("103", "2000");
            fail("삭제 실패");
        } catch (Exception e) {

        }
    }
}
