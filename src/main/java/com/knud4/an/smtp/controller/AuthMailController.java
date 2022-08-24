package com.knud4.an.smtp.controller;

import com.knud4.an.auth.dto.CodeMailDTO;
import com.knud4.an.smtp.service.AuthMailService;
import com.knud4.an.utils.api.ApiUtil;
import com.knud4.an.utils.api.ApiUtil.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthMailController {
    private final AuthMailService authMailService;

    @Operation(summary = "인증 메일 발송")
    @PostMapping("/api/v1/mail/code")
    public ApiSuccessResult<String> sendAuthenticationCode(
            @RequestBody CodeMailDTO dto) throws IllegalStateException {
        authMailService.sendAuthenticationCode(dto.getEmail());
        return ApiUtil.success("성공적으로 메일이 발송되었습니다.");
    }

    @Operation(summary = "인증 메일 재발송")
    @PostMapping("/api/v1/mail/code/resend")
    public ApiSuccessResult<String> resendAuthenticationCode(
            @RequestBody CodeMailDTO dto) throws IllegalStateException {
        authMailService.resendAuthenticationCode(dto.getEmail());
        return ApiUtil.success("성공적으로 메일이 발송되었습니다.");
    }
}
