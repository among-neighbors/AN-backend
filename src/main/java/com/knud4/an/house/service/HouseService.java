package com.knud4.an.house.service;

import com.knud4.an.house.dto.CreateHouseDTO;
import com.knud4.an.house.dto.DeleteHouseDTO;
import com.knud4.an.house.dto.FindHouseDTO;
import com.knud4.an.house.dto.UpdateHouseDTO;
import com.knud4.an.house.entity.House;
import com.knud4.an.house.repository.HouseRepository;
import com.knud4.an.line.dto.FindLineDTO;
import com.knud4.an.line.entity.Line;
import com.knud4.an.line.repository.LineRepository;
import com.knud4.an.line.service.LineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HouseService {

    private final LineRepository lineRepository;
    private final HouseRepository houseRepository;

    @Transactional
    public void createHouse(CreateHouseDTO createHouseDTO) {
        Line findLine = lineRepository.findByName(createHouseDTO.getLineName());
        House createHouse = House.builder()
                .name(createHouseDTO.getHouseName())
                .build();

        createHouse.setLine(findLine);
        houseRepository.save(createHouse);
    }

    @Transactional
    public void deleteHouse(DeleteHouseDTO deleteHouseDTO) {
        Line findLine = lineRepository.findByName(deleteHouseDTO.getLineName());
        houseRepository.deleteByName(findLine.getId(), deleteHouseDTO.getHouseName());
    }

    @Transactional
    public void updateHouseName(UpdateHouseDTO updateHouseDTO) {
        Line findLine = lineRepository.findByName(updateHouseDTO.getLineName());
        houseRepository.updateHouseName(findLine.getId(),
                updateHouseDTO.getOldHouseName(), updateHouseDTO.getChangeHouseName());
    }

    public List<FindHouseDTO> findHousesByLineName(String lineName) {
        Line findLine = lineRepository.findByName(lineName);
        List<House> findHouses = houseRepository.findHousesByLineName(findLine.getId());

        List<FindHouseDTO> findHouseDTOS = new ArrayList<>();

        for (House house : findHouses) {
            findHouseDTOS.add(new FindHouseDTO(house.getName()));
        }

        return findHouseDTOS;
    }
}
