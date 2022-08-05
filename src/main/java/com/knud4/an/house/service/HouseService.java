package com.knud4.an.house.service;

import com.knud4.an.exceptions.NotFoundException;
import com.knud4.an.house.dto.CreateHouseForm;
import com.knud4.an.house.entity.House;
import com.knud4.an.house.repository.HouseRepository;
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
    public Long createHouse(CreateHouseForm createHouseForm) {
        Line findLine = getFindLine(createHouseForm.getLineName());

        House createHouse = House.builder()
                .name(createHouseForm.getHouseName())
                .line(findLine)
                .build();

        houseRepository.save(createHouse);

        return createHouse.getId();
    }

    public House findHouseById(Long id) {
        return houseRepository.findById(id);
    }

    public House findByLineNameAndHouseName(String lineName, String houseName) {
        Line findLine = getFindLine(lineName);

        return houseRepository.findByLineIdAndHouseName(findLine.getId(), houseName)
                .orElseThrow(() -> new NotFoundException("세대를 찾을 수 없습니다."));
    }

    public List<House> findHousesByLineName(String lineName) {
        Line findLine = getFindLine(lineName);

        return houseRepository.findHousesByLineId(findLine.getId());
    }

    private Line getFindLine(String lineName) {
        return lineRepository.findByName(lineName)
                .orElseThrow(() -> new NotFoundException("라인을 찾을 수 없습니다."));
    }
}
