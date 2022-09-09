package com.knud4.an.house.dto;

import com.knud4.an.house.entity.House;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class HouseListResponse {
    private final List<HouseDTO> houses;

    public HouseListResponse(List<House> houseList) {
        this.houses = houseList.stream()
                .map(h -> new HouseDTO(h.getId(), h.getLine().getId(), h.getName()))
                .collect(Collectors.toList());
    }
}
