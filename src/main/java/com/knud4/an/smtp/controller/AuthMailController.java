package com.knud4.an.smtp.controller;

import com.knud4.an.smtp.service.AuthMailService;
import com.knud4.an.utils.api.ApiUtil;
import com.knud4.an.utils.api.ApiUtil.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthMailController {
    private final AuthMailService authMailService;

    @PostMapping("/api/v1/mail/code")
    public ApiSuccessResult<String> sendAuthenticationCode(
            @RequestParam(name = "email") String email) throws IllegalStateException {
        authMailService.sendAuthenticationCode(email);
        return ApiUtil.success("성공적으로 메일이 발송되었습니다.");
    }

    @PostMapping("/api/v1/mail/re-code")
    public ApiSuccessResult<String> resendAuthenticationCode(
            @RequestParam(name = "email") String email) throws IllegalStateException {
        authMailService.resendAuthenticationCode(email);
        return ApiUtil.success("성공적으로 메일이 발송되었습니다.");
    }
}
