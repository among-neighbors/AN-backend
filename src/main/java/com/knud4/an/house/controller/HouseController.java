package com.knud4.an.house.controller;

import com.knud4.an.house.dto.CreateHouseForm;
import com.knud4.an.house.dto.HouseListResponse;
import com.knud4.an.house.service.HouseService;
import com.knud4.an.utils.api.ApiUtil;
import com.knud4.an.utils.api.ApiUtil.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class HouseController {
    private final HouseService houseService;

    @Operation(summary = "세대 추가")
    @PostMapping("/api/v1/house/new")
    public ApiSuccessResult<Long> createHouse(@RequestBody @Valid CreateHouseForm form) {
        Long houseId = houseService.createHouse(form);

        return ApiUtil.success(houseId);
    }

    @Operation(summary = "세대 목록 조회")
    @GetMapping("/api/v1/house")
    public ApiSuccessResult<HouseListResponse> searchHouseList(@RequestParam(name = "lineName") String lineName) {
        return ApiUtil.success(new HouseListResponse(houseService.findHousesByLineName(lineName)));
    }
}
