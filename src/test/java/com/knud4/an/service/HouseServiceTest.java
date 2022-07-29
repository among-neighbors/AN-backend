package com.knud4.an.service;

import com.knud4.an.house.dto.CreateHouseDTO;
import com.knud4.an.house.dto.DeleteHouseDTO;
import com.knud4.an.house.dto.FindHouseDTO;
import com.knud4.an.house.dto.UpdateHouseDTO;
import com.knud4.an.house.service.HouseService;
import com.knud4.an.line.dto.CreateLineDTO;
import com.knud4.an.line.service.LineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class HouseServiceTest {

    @Autowired
    private HouseService houseService;
    @Autowired
    private LineService lineService;

    @BeforeEach
    public void createLine() {
        CreateLineDTO createLineDTO = new CreateLineDTO();
        createLineDTO.setName("101동");

        lineService.createLine(createLineDTO);
    }

    @Test
    public void createHouse() {
        CreateHouseDTO createHouseDTO = new CreateHouseDTO();
        createHouseDTO.setLineName("101동");
        createHouseDTO.setHouseName("206호");

        houseService.createHouse(createHouseDTO);
        List<FindHouseDTO> findHouses = houseService.findHousesByLineName("101동");

        for (FindHouseDTO findHouseDTO : findHouses) {
            System.out.println(findHouseDTO.getHouseName());
        }
    }

    @Test
    public void deleteHouse() {
        CreateHouseDTO createHouseDTO = new CreateHouseDTO();
        createHouseDTO.setLineName("101동");
        createHouseDTO.setHouseName("206호");

        houseService.createHouse(createHouseDTO);

        DeleteHouseDTO deleteHouseDTO = new DeleteHouseDTO();
        deleteHouseDTO.setLineName("101동");
        deleteHouseDTO.setHouseName("206호");

        List<FindHouseDTO> findHouses = houseService.findHousesByLineName("101동");

        assertThat(findHouses.size()).isEqualTo(1);

        houseService.deleteHouse(deleteHouseDTO);

        findHouses = houseService.findHousesByLineName("101동");

        assertThat(findHouses.size()).isEqualTo(0);
    }

    @Test
    public void updateHouseName() {
        CreateHouseDTO createHouseDTO = new CreateHouseDTO();
        createHouseDTO.setLineName("101동");
        createHouseDTO.setHouseName("206호");

        houseService.createHouse(createHouseDTO);

        UpdateHouseDTO updateHouseDTO = new UpdateHouseDTO();
        updateHouseDTO.setLineName("101동");
        updateHouseDTO.setOldHouseName("206호");
        updateHouseDTO.setChangeHouseName("207호");

        houseService.updateHouseName(updateHouseDTO);

        List<FindHouseDTO> findHouses = houseService.findHousesByLineName("101동");

        assertThat(findHouses.get(0).getHouseName()).isEqualTo("207호");
    }
}
