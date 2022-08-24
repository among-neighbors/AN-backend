package com.knud4.an.line.controller;

import com.knud4.an.line.dto.CreateLineForm;
import com.knud4.an.line.dto.CreateLineResponse;
import com.knud4.an.line.dto.LineListDTO;
import com.knud4.an.line.service.LineService;
import com.knud4.an.security.provider.JwtProvider;
import com.knud4.an.utils.api.ApiUtil;
import com.knud4.an.utils.api.ApiUtil.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class LineController {
    private final LineService lineService;
    private final JwtProvider jwtProvider;

    @Operation(summary = "라인 추가")
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
}
