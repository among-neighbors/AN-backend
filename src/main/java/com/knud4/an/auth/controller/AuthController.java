package com.knud4.an.auth.controller;

import com.knud4.an.account.entity.Account;
import com.knud4.an.account.entity.Profile;
import com.knud4.an.auth.dto.account.SignInAccountForm;
import com.knud4.an.auth.dto.account.SignInAccountResponse;
import com.knud4.an.auth.dto.account.SignUpAccountForm;
import com.knud4.an.auth.dto.account.SignUpAccountResponse;
import com.knud4.an.auth.dto.profile.AddProfileForm;
import com.knud4.an.auth.dto.profile.AddProfileResponse;
import com.knud4.an.auth.dto.profile.SignInProfileForm;
import com.knud4.an.auth.dto.profile.SignInProfileResponse;
import com.knud4.an.auth.service.AuthService;
import com.knud4.an.exception.NotFoundException;
import com.knud4.an.utils.api.ApiUtil;
import com.knud4.an.utils.api.ApiUtil.*;
import com.knud4.an.utils.cookie.CookieUtil;
import com.knud4.an.security.provider.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;

    @Operation(summary = "계정 가입")
    @PostMapping("/api/v1/auth/account/new")
    public ApiSuccessResult<SignUpAccountResponse> signUpAccount (
            @RequestBody @Valid SignUpAccountForm form) throws RuntimeException {

        Long id = authService.signUpAccount(form);
        return ApiUtil.success(new SignUpAccountResponse(id));
    }

    @Operation(summary = "계정 로그인")
    @PostMapping("/api/v1/auth/account/login")
    public ApiSuccessResult<SignInAccountResponse> signInAccount (
            @RequestBody @Valid SignInAccountForm form,
            HttpServletResponse res) throws RuntimeException {

        Account account = authService.signInAccount(form);

        String accessToken = jwtProvider.generateAccountToken(account.getEmail(), account.getId()+"");

        ResponseCookie cookie = cookieUtil.createCookie(JwtProvider.ACCOUNT_TOKEN_NAME, accessToken);
        res.addHeader("Set-Cookie", cookie.toString());

        return ApiUtil.success(new SignInAccountResponse
                (account.getId(), account.getLine().getId(), account.getHouse().getId())) ;
    }

    @Operation(summary = "프로필 추가")
    @PostMapping("/api/v1/auth/profile/new")
    public ApiSuccessResult<AddProfileResponse> addProfile(
            @RequestBody @Valid AddProfileForm form,
            HttpServletRequest req) throws RuntimeException {

        String email = (String)req.getAttribute("email");

        Long profileId = authService.AddProfile(form, email);

        return ApiUtil.success(new AddProfileResponse(profileId));
    }

    @Operation(summary = "프로필 로그인")
    @PostMapping("/api/v1/auth/profile/login")
    public ApiSuccessResult<SignInProfileResponse> signInProfile(
            @RequestBody @Valid SignInProfileForm form,
            HttpServletRequest req,
            HttpServletResponse res) throws RuntimeException {

        String email = (String)req.getAttribute("email");

        Profile profile = authService.signInProfile(form, email);

        String accessToken = jwtProvider.generateProfileToken(
                profile.getAccount().getEmail(),
                profile.getAccount().getId()+"",
                profile.getId() + "");
        ResponseCookie cookie = cookieUtil.createCookie(JwtProvider.PROFILE_TOKEN_NAME, accessToken);
        res.addHeader("Set-Cookie", cookie.toString());

        return ApiUtil.success(new SignInProfileResponse(
                profile.getAccount().getId(), profile.getId(), profile.getName()
        ));
    }

    @Operation(summary = "이메일 인증 코드 입력")
    @PostMapping("/api/v1/auth/verify-code")
    public ApiSuccessResult<String> verifyCode(@RequestParam(name = "email") String email,
                                               @RequestParam(name = "code") String code) throws NotFoundException {
        authService.verifySignUpCode(email, code);
        return ApiUtil.success("인증이 완료되었습니다.");
    }

}
