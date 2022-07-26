package com.knud4.an.house.controller;

import com.knud4.an.house.dto.CreateHouseForm;
import com.knud4.an.house.dto.HouseListResponse;
import com.knud4.an.house.service.HouseService;
import com.knud4.an.utils.api.ApiUtil;
import com.knud4.an.utils.api.ApiUtil.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "세대")
@RestController
@RequiredArgsConstructor
public class HouseController {
    private final HouseService houseService;

    @Operation(summary = "세대 추가", description = "account token이 필요합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "세대 추가 성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "이미 존재하는 세대입니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class))),
    })
    @PostMapping("/api/v1/manager/houses/new")
    public ApiSuccessResult<Long> createHouse(@RequestBody @Valid CreateHouseForm form) {
        Long houseId = houseService.createHouse(form);
        return ApiUtil.success(houseId);
    }

//    @Operation(summary = "세대 삭제")
//    @DeleteMapping("/api/v1/manager/houses")
//    public ApiSuccessResult<String> removeHouse(@RequestBody RemoveHouseDTO dto) {
//        houseService.removeHouseById(dto.getHouseId());
//        return ApiUtil.success("성공적으로 삭제 되었습니다.");
//    }

    @Operation(summary = "세대 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "세대 목록 조회 성공", content = @Content(schema = @Schema(implementation = HouseListResponse.class))),
    })
    @GetMapping("/api/v1/houses")
    public ApiSuccessResult<HouseListResponse> searchHouseList(@RequestParam(name = "lineName") String lineName) {
        return ApiUtil.success(new HouseListResponse(houseService.findHousesByLineName(lineName)));
    }
}
