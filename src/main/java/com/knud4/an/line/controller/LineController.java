package com.knud4.an.line.controller;

import com.knud4.an.house.dto.HouseListResponse;
import com.knud4.an.line.dto.CreateLineForm;
import com.knud4.an.line.dto.CreateLineResponse;
import com.knud4.an.line.dto.LineListDTO;
import com.knud4.an.line.service.LineService;
import com.knud4.an.security.provider.JwtProvider;
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

@Tag(name = "라인")
@RestController
@RequiredArgsConstructor
public class LineController {
    private final LineService lineService;
    private final JwtProvider jwtProvider;

    @Operation(summary = "라인 추가", description = "account token이 필요합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "라인 추가 성공", content = @Content(schema = @Schema(implementation = CreateLineResponse.class))),
            @ApiResponse(responseCode = "400", description = "이미 존재하는 라인입니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class)))
    })
    @PostMapping("/api/v1/manager/lines/new")
    public ApiSuccessResult<CreateLineResponse> createLine
            (@Valid @RequestBody CreateLineForm form) throws RuntimeException{
        Long lineId = lineService.createLine(form);

        return ApiUtil.success(new CreateLineResponse(lineId));
    }

    @Operation(summary = "라인 목록 조회")
    @GetMapping("/api/v1/lines")
    public ApiSuccessResult<LineListDTO> findAllLines() {
        return ApiUtil.success(new LineListDTO(lineService.findAllLines()));
    }
//
//    @Operation(summary = "라인 삭제")
//    @DeleteMapping("/api/v1/lines")
//    public void removeLine(@RequestParam Long lineId) {
//        lineService.removeLine(lineId);
//    }
}
