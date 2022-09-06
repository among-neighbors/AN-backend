package com.knud4.an.auth.controller;

import com.knud4.an.account.entity.Account;
import com.knud4.an.account.entity.Profile;
import com.knud4.an.auth.dto.TokenDTO;
import com.knud4.an.auth.dto.CodeVerificationDTO;
import com.knud4.an.auth.dto.account.SignInAccountForm;
import com.knud4.an.auth.dto.account.SignInAccountResponse;
import com.knud4.an.auth.dto.account.SignUpAccountForm;
import com.knud4.an.auth.dto.account.SignUpAccountResponse;
import com.knud4.an.auth.dto.manager.SignInManagerResponse;
import com.knud4.an.auth.dto.profile.AddProfileForm;
import com.knud4.an.auth.dto.profile.AddProfileResponse;
import com.knud4.an.auth.dto.profile.SignInProfileForm;
import com.knud4.an.auth.dto.profile.SignInProfileResponse;
import com.knud4.an.auth.service.AuthService;
import com.knud4.an.exception.NotFoundException;
import com.knud4.an.annotation.AccountRequired;
import com.knud4.an.utils.api.ApiUtil;
import com.knud4.an.utils.api.ApiUtil.*;
import com.knud4.an.utils.cookie.CookieUtil;
import com.knud4.an.security.provider.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Tag(name = "인증")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;

    @Operation(summary = "계정 가입")
    @PostMapping("/api/v1/auth/accounts/new")
    public ApiSuccessResult<SignUpAccountResponse> signUpAccount (
            @RequestBody @Valid SignUpAccountForm form) throws RuntimeException {

        Long id = authService.signUpAccount(form);
        return ApiUtil.success(new SignUpAccountResponse(id));
    }

    @Operation(summary = "계정 로그인")
    @PostMapping("/api/v1/auth/accounts/login")
    public ApiSuccessResult<SignInAccountResponse> signInAccount (
            @RequestBody @Valid SignInAccountForm form,
            HttpServletResponse res) throws RuntimeException {

        Account account = authService.signInAccount(form);

        String accessToken = jwtProvider.generateAccountToken(account.getEmail(), account.getId()+"");
        String refreshToken = jwtProvider.generateAccountRefreshToken(account.getEmail(), account.getId() + "");

        ResponseCookie cookie = cookieUtil.createRefreshTokenCookie(JwtProvider.ACCOUNT_TOKEN_NAME, refreshToken);
        res.addHeader("Set-Cookie", cookie.toString());

        return ApiUtil.success(new SignInAccountResponse(account, accessToken, refreshToken));
    }

    @Operation(summary = "매니저 로그인")
    @PostMapping("/api/v1/auth/manager/login")
    public ApiSuccessResult<?> signInManager (
            @RequestBody @Valid SignInAccountForm form,
            HttpServletResponse res) throws RuntimeException {

        Profile profile = authService.signInManager(form);
        Account account = profile.getAccount();

        String accountAccessToken = jwtProvider.generateAccountToken(
                account.getEmail(),
                account.getId()+"");
        String accountRefreshToken = jwtProvider.generateAccountRefreshToken(
                account.getEmail(),
                account.getId() + "");

        String profileAccessToken = jwtProvider.generateProfileToken(
                profile.getAccount().getEmail(),
                profile.getAccount().getId()+"",
                profile.getId() + "");

        String profileRefreshToken = jwtProvider.generateProfileRefreshToken(
                profile.getAccount().getEmail(),
                profile.getAccount().getId() + "",
                profile.getId() + "");

        ResponseCookie accountCookie = cookieUtil.createRefreshTokenCookie(JwtProvider.ACCOUNT_TOKEN_NAME, accountRefreshToken);
        ResponseCookie profileCookie = cookieUtil.createRefreshTokenCookie(JwtProvider.PROFILE_TOKEN_NAME, profileRefreshToken);

        res.addHeader("Set-Cookie", accountCookie.toString());
        res.addHeader("Set-Cookie", profileCookie.toString());

        return ApiUtil.success(new SignInManagerResponse(
                accountAccessToken, accountRefreshToken,
                profileAccessToken, profileRefreshToken));
    }

    @AccountRequired
    @Operation(summary = "프로필 추가", description = "account token이 필요합니다.")
    @PostMapping("/api/v1/auth/profiles/new")
    public ApiSuccessResult<AddProfileResponse> addProfile(
            @RequestBody @Valid AddProfileForm form,
            HttpServletRequest req) throws RuntimeException {

        String email = (String)req.getAttribute("email");

        Long profileId = authService.AddProfile(form, email);

        return ApiUtil.success(new AddProfileResponse(profileId));
    }

    @AccountRequired
    @Operation(summary = "프로필 로그인", description = "account token이 필요합니다.")
    @PostMapping("/api/v1/auth/profiles/login")
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

        String refreshToken = jwtProvider.generateProfileRefreshToken(
                profile.getAccount().getEmail(),
                profile.getAccount().getId() + "",
                profile.getId() + "");

        ResponseCookie cookie = cookieUtil.createRefreshTokenCookie(JwtProvider.PROFILE_TOKEN_NAME, refreshToken);
        res.addHeader("Set-Cookie", cookie.toString());

        return ApiUtil.success(new SignInProfileResponse(profile, accessToken, refreshToken));
    }

    @Operation(summary = "계정 로그아웃")
    @GetMapping("/api/v1/auth/accounts/logout")
    public ApiSuccessResult<String> signOutAccount(
            HttpServletResponse res) {
        ResponseCookie deleteCookie
                = cookieUtil.createRefreshTokenCookie(JwtProvider.ACCOUNT_TOKEN_NAME, null, 0);
        res.addHeader("Set-Cookie", deleteCookie.toString());

        return ApiUtil.success("성공적으로 로그아웃 했습니다.");
    }

    @Operation(summary = "프로필 로그아웃")
    @GetMapping("/api/v1/auth/profiles/logout")
    public ApiSuccessResult<String> signOutProfile(
            HttpServletResponse res) {
        ResponseCookie deleteCookie
                = cookieUtil.createRefreshTokenCookie(JwtProvider.PROFILE_TOKEN_NAME, null, 0);
        res.addHeader("Set-Cookie", deleteCookie.toString());

        return ApiUtil.success("성공적으로 로그아웃 했습니다.");
    }

    @Operation(summary = "이메일 인증 코드 입력")
    @PostMapping("/api/v1/auth/verify-code")
    public ApiSuccessResult<String> verifyCode(@RequestBody @Valid CodeVerificationDTO verification) throws NotFoundException {
        authService.verifySignUpCode(verification.getEmail(), verification.getCode());
        return ApiUtil.success("인증이 완료되었습니다.");
    }

    @Operation(summary = "계정 토큰 재발급")
    @PostMapping("/api/v1/auth/account-token")
    public ApiSuccessResult<TokenDTO> reIssueAccountToken(HttpServletRequest req, HttpServletResponse res) {
//      쿠키로 전달된 refresh 토큰 확인
        String refreshToken = cookieUtil.getCookie(req, "account_refresh_token").getValue();
//      refresh token 이 유효한 상태면 access token 발급
        String accessToken = jwtProvider.reIssueAccountToken(refreshToken);

        String reIssuedRefreshToken = jwtProvider.reIssueAccountRefreshToken(refreshToken);
        if (reIssuedRefreshToken != null) {
            ResponseCookie refreshTokenCookie
                    = cookieUtil.createRefreshTokenCookie(JwtProvider.ACCOUNT_TOKEN_NAME, reIssuedRefreshToken);
            res.addHeader("Set-Cookie", refreshTokenCookie.toString());
        }
        return ApiUtil.success(new TokenDTO(accessToken, reIssuedRefreshToken));
    }

    @Operation(summary = "프로필 토큰 재발급")
    @PostMapping("/api/v1/auth/profile-token")
    public ApiSuccessResult<TokenDTO> reIssueProfileToken(HttpServletRequest req, HttpServletResponse res) {
//      쿠키로 전달된 refresh 토큰 확인
        String refreshToken = cookieUtil.getCookie(req, "profile_refresh_token").getValue();
//      유효한 상태면 access token 발급
        String accessToken = jwtProvider.reIssueProfileToken(refreshToken);

        String reIssuedRefreshToken = jwtProvider.reIssueProfileRefreshToken(refreshToken);
//      갱신 가능한 상태면 재발급
        if (reIssuedRefreshToken != null) {
            ResponseCookie refreshTokenCookie
                    = cookieUtil.createRefreshTokenCookie(JwtProvider.PROFILE_TOKEN_NAME, reIssuedRefreshToken);
            res.addHeader("Set-Cookie", refreshTokenCookie.toString());
        }
        return ApiUtil.success(new TokenDTO(accessToken, reIssuedRefreshToken));
    }
}
