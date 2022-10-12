package com.knud4.an.expression.controller;

import com.knud4.an.annotation.ProfileRequired;
import com.knud4.an.expression.dto.CreateExpressionForm;
import com.knud4.an.expression.dto.ExpressionDTO;
import com.knud4.an.expression.service.ExpressionService;
import com.knud4.an.utils.api.ApiUtil;
import com.knud4.an.utils.api.ApiUtil.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ExpressionController {

    private final ExpressionService expressionService;

    @ProfileRequired
    @Operation(summary = "커뮤니티글 좋아요", description = "profile token이 필요합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "커뮤니티글 좋아요 성공", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "커뮤니티글이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiUtil.ApiErrorResult.class)))
    })
    @PostMapping("/api/v1/expressions")
    public ApiSuccessResult<String> updateExpression(@RequestBody CreateExpressionForm form,
                                                     HttpServletRequest req) {
        Long profileId = (Long) req.getAttribute("profileId");
        expressionService.save(form, profileId);
        return ApiUtil.success("좋아요가 업데이트 되었습니다.");
    }

    @ProfileRequired
    @Operation(summary = "커뮤니티글 좋아요", description = "profile token이 필요합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "커뮤니티글 좋아요 성공", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "커뮤니티글이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiUtil.ApiErrorResult.class)))
    })
    @GetMapping("/api/v1/expressions/{id}")
    public ApiSuccessResult<ExpressionDTO> findExpression(@PathVariable Long id,
                                                          HttpServletRequest req) {
        Long profileId = (Long) req.getAttribute("profileId");
        return ApiUtil.success(new ExpressionDTO(
                expressionService.findLikeCountByBoardId(id),
                expressionService.didILiked(id, profileId)
        ));
    }
}
