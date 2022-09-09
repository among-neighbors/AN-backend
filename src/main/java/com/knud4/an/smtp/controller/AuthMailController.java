package com.knud4.an.smtp.controller;

import com.knud4.an.auth.dto.CodeMailDTO;
import com.knud4.an.smtp.service.AuthMailService;
import com.knud4.an.utils.api.ApiUtil;
import com.knud4.an.utils.api.ApiUtil.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "메일")
@RestController
@RequiredArgsConstructor
public class AuthMailController {
    private final AuthMailService authMailService;

    @Operation(summary = "인증 메일 발송")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증 메일 발송 성공", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "이미 인증코드가 발송되었습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class)))
    })
    @PostMapping("/api/v1/mail/code")
    public ApiSuccessResult<String> sendAuthenticationCode(
            @RequestBody CodeMailDTO dto) throws IllegalStateException {
        authMailService.sendAuthenticationCode(dto.getEmail());
        return ApiUtil.success("성공적으로 메일이 발송되었습니다.");
    }

    @Operation(summary = "인증 메일 재발송")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증 메일 재발송 성공", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "잠시 후에 다시 시도해주세요.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class)))
    })
    @PostMapping("/api/v1/mail/code/resend")
    public ApiSuccessResult<String> resendAuthenticationCode(
            @RequestBody CodeMailDTO dto) throws IllegalStateException {
        authMailService.resendAuthenticationCode(dto.getEmail());
        return ApiUtil.success("성공적으로 메일이 발송되었습니다.");
    }
}
