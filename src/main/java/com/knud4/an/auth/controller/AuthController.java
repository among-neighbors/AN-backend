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
import com.knud4.an.exception.CookieNotFoundException;
import com.knud4.an.exception.NotFoundException;
import com.knud4.an.annotation.AccountRequired;
import com.knud4.an.utils.api.ApiUtil;
import com.knud4.an.utils.api.ApiUtil.*;
import com.knud4.an.utils.cookie.CookieUtil;
import com.knud4.an.security.provider.JwtProvider;
import com.knud4.an.utils.redis.RedisUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Tag(name = "인증")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;

    private final RedisUtil redisUtil;

    @Operation(summary = "계정 가입")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "계정 가입 성공", content = @Content(schema = @Schema(implementation = SignUpAccountResponse.class))),
        @ApiResponse(responseCode = "404", description = "라인 또는 세대를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class)))
    })
    @PostMapping("/api/v1/auth/accounts/new")
    public ApiSuccessResult<SignUpAccountResponse> signUpAccount (
            @RequestBody @Valid SignUpAccountForm form) throws RuntimeException {

        Long id = authService.signUpAccount(form);
        return ApiUtil.success(new SignUpAccountResponse(id));
    }

    @Operation(summary = "계정 로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "계정 로그인 성공", content = @Content(schema = @Schema(implementation = SignInAccountResponse.class))),
            @ApiResponse(responseCode = "400", description = "비밀번호가 잘못되었습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class))),
            @ApiResponse(responseCode = "404", description = "계정이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class)))
    })
    @PostMapping("/api/v1/auth/accounts/login")
    public ApiSuccessResult<SignInAccountResponse> signInAccount (
            @RequestBody @Valid SignInAccountForm form,
            HttpServletRequest req,
            HttpServletResponse res) throws RuntimeException {

        Account account = authService.signInAccount(form);

        String accessToken = jwtProvider.generateAccountToken(account.getEmail(), account.getId()+"");
        String refreshToken = jwtProvider.generateAccountRefreshToken(account.getEmail(), account.getId() + "");

        ResponseCookie cookie = cookieUtil.createCookie(JwtProvider.ACCOUNT_TOKEN_NAME, refreshToken);
        res.addHeader("Set-Cookie", cookie.toString());

        ApiSuccessResult<SignInAccountResponse> result = ApiUtil
                .success(new SignInAccountResponse(account, accessToken, refreshToken));

        try {
            String accountRefreshToken = cookieUtil.getCookie(req, JwtProvider.ACCOUNT_TOKEN_NAME).getValue();
            redisUtil.del(accountRefreshToken);
        } catch (CookieNotFoundException ignored) {
        } finally {
            cacheToken(accessToken, refreshToken);
        }

        return result;
    }

    @AccountRequired
    @Operation(summary = "프로필 로그인", description = "account token이 필요합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 로그인 성공", content = @Content(schema = @Schema(implementation = SignInProfileResponse.class))),
            @ApiResponse(responseCode = "400", description = "계정 정보가 잘못되었습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class))),
            @ApiResponse(responseCode = "404", description = "프로필 또는 핀 번호를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class)))
    })
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

        ResponseCookie cookie = cookieUtil.createCookie(JwtProvider.PROFILE_TOKEN_NAME, refreshToken);
        res.addHeader("Set-Cookie", cookie.toString());

        ApiSuccessResult<SignInProfileResponse> result = ApiUtil
                .success(new SignInProfileResponse(profile, accessToken, refreshToken));

        try {
            String profileRefreshToken = cookieUtil.getCookie(req, JwtProvider.PROFILE_TOKEN_NAME).getValue();
            redisUtil.del(profileRefreshToken);
        } catch (CookieNotFoundException ignored) {
        } finally {
            cacheToken(accessToken, refreshToken);
        }

        return result;
    }

    @Operation(summary = "매니저 로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "매니저 로그인 성공", content = @Content(schema = @Schema(implementation = SignInManagerResponse.class))),
            @ApiResponse(responseCode = "400", description = "매니저 계정이 아닙니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class))),
            @ApiResponse(responseCode = "404", description = "매니저 프로필 누락", content = @Content(schema = @Schema(implementation = ApiErrorResult.class)))
    })
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

        ResponseCookie accountCookie = cookieUtil.createCookie(JwtProvider.ACCOUNT_TOKEN_NAME, accountRefreshToken);
        ResponseCookie profileCookie = cookieUtil.createCookie(JwtProvider.PROFILE_TOKEN_NAME, profileRefreshToken);

        res.addHeader("Set-Cookie", accountCookie.toString());
        res.addHeader("Set-Cookie", profileCookie.toString());

        return ApiUtil.success(new SignInManagerResponse(
                accountAccessToken, accountRefreshToken,
                profileAccessToken, profileRefreshToken));
    }

    @AccountRequired
    @Operation(summary = "프로필 추가", description = "account token이 필요합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 추가 성공", content = @Content(schema = @Schema(implementation = AddProfileResponse.class))),
            @ApiResponse(responseCode = "400", description = "계정이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class))),
            @ApiResponse(responseCode = "404", description = "프로필 이름이 중복되었습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class)))
    })
    @PostMapping("/api/v1/auth/profiles/new")
    public ApiSuccessResult<AddProfileResponse> addProfile(
             @Valid AddProfileForm form,
            HttpServletRequest req) throws RuntimeException, IOException {

        String email = (String)req.getAttribute("email");

        Long profileId = authService.AddProfile(form, email);

        return ApiUtil.success(new AddProfileResponse(profileId));
    }

    @Operation(summary = "계정 로그아웃")
    @ApiResponse(responseCode = "200", description = "계정 로그아웃 성공", content = @Content(schema = @Schema(implementation = String.class)))
    @GetMapping("/api/v1/auth/accounts/logout")
    public ApiSuccessResult<String> signOutAccount(
            HttpServletRequest req,
            HttpServletResponse res) {

        try {
            String accountRefreshToken = cookieUtil.getCookie(req, JwtProvider.ACCOUNT_TOKEN_NAME).getValue();
            redisUtil.del(accountRefreshToken);

            String profileRefreshToken = cookieUtil.getCookie(req, JwtProvider.PROFILE_TOKEN_NAME).getValue();
            redisUtil.del(profileRefreshToken);
        } catch (CookieNotFoundException ignored) {
        }

        ResponseCookie deleteAccountCookie
                = cookieUtil.createCookie(JwtProvider.ACCOUNT_TOKEN_NAME, null, 0);
        res.addHeader("Set-Cookie", deleteAccountCookie.toString());

        ResponseCookie deleteProfileCookie
                = cookieUtil.createCookie(JwtProvider.PROFILE_TOKEN_NAME, null, 0);
        res.addHeader("Set-Cookie", deleteProfileCookie.toString());

        return ApiUtil.success("성공적으로 로그아웃 했습니다.");
    }

    @Operation(summary = "프로필 로그아웃")
    @ApiResponse(responseCode = "200", description = "프로필 로그아웃 성공", content = @Content(schema = @Schema(implementation = String.class)))
    @GetMapping("/api/v1/auth/profiles/logout")
    public ApiSuccessResult<String> signOutProfile(
            HttpServletRequest req,
            HttpServletResponse res) {

        try {
            String profileRefreshToken = cookieUtil.getCookie(req, JwtProvider.PROFILE_TOKEN_NAME).getValue();
            redisUtil.del(profileRefreshToken);
        } catch (CookieNotFoundException ignored) {
        }

        ResponseCookie deleteCookie
                = cookieUtil.createCookie(JwtProvider.PROFILE_TOKEN_NAME, null, 0);
        res.addHeader("Set-Cookie", deleteCookie.toString());

        return ApiUtil.success("성공적으로 로그아웃 했습니다.");
    }

    @Operation(summary = "이메일 인증 코드 입력")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이메일 인증 성공", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "코드가 잘못되었습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class)))
    })
    @PostMapping("/api/v1/auth/verify-code")
    public ApiSuccessResult<String> verifyCode(@RequestBody @Valid CodeVerificationDTO verification) throws NotFoundException {
        authService.verifySignUpCode(verification.getEmail(), verification.getCode());
        return ApiUtil.success("인증이 완료되었습니다.");
    }

    @Operation(summary = "계정 토큰 재발급")
    @ApiResponse(responseCode = "200", description = "계정 토큰 재발급 성공", content = @Content(schema = @Schema(implementation = TokenDTO.class)))
    @PostMapping("/api/v1/auth/account-token")
    public ApiSuccessResult<TokenDTO> reIssueAccountToken(HttpServletRequest req, HttpServletResponse res) {
        String accessToken;
        String refreshToken;

        refreshToken = cookieUtil.getCookie(req, JwtProvider.ACCOUNT_TOKEN_NAME).getValue();

        String reIssuedRefreshToken = jwtProvider.reIssueAccountRefreshToken(refreshToken);

        if (reIssuedRefreshToken != null) {
            redisUtil.del(refreshToken);

            refreshToken = reIssuedRefreshToken;
            accessToken = jwtProvider.reIssueAccountToken(refreshToken);

            cacheToken(accessToken, refreshToken);

            ResponseCookie refreshTokenCookie
                    = cookieUtil.createCookie(JwtProvider.ACCOUNT_TOKEN_NAME, refreshToken);

            res.addHeader("Set-Cookie", refreshTokenCookie.toString());
        } else {

            String cachedToken = (String) redisUtil.get(refreshToken);

            if (cachedToken == null) {
                accessToken = jwtProvider.reIssueAccountToken(refreshToken);
                cacheToken(accessToken, refreshToken);
            } else {
                accessToken = cachedToken;
            }
        }

        return ApiUtil.success(new TokenDTO(accessToken, reIssuedRefreshToken));
    }

    @Operation(summary = "프로필 토큰 재발급")
    @ApiResponse(responseCode = "200", description = "프로필 토큰 재발급 성공", content = @Content(schema = @Schema(implementation = TokenDTO.class)))
    @PostMapping("/api/v1/auth/profile-token")
    public ApiSuccessResult<TokenDTO> reIssueProfileToken(HttpServletRequest req, HttpServletResponse res) {

        String accessToken;
        String refreshToken;

        refreshToken = cookieUtil.getCookie(req, JwtProvider.PROFILE_TOKEN_NAME).getValue();

        String reIssuedRefreshToken = jwtProvider.reIssueProfileRefreshToken(refreshToken);

        if (reIssuedRefreshToken != null) {
            redisUtil.del(refreshToken);

            refreshToken = reIssuedRefreshToken;
            accessToken = jwtProvider.reIssueProfileToken(refreshToken);
            cacheToken(accessToken, refreshToken);

            ResponseCookie refreshTokenCookie
                    = cookieUtil.createCookie(JwtProvider.PROFILE_TOKEN_NAME, reIssuedRefreshToken);
            res.addHeader("Set-Cookie", refreshTokenCookie.toString());
        } else {
            String cachedToken = (String) redisUtil.get(refreshToken);

            if (cachedToken == null) {
                accessToken = jwtProvider.reIssueProfileToken(refreshToken);
                cacheToken(accessToken, refreshToken);
            } else {
                accessToken = cachedToken;
            }
        }

        return ApiUtil.success(new TokenDTO(accessToken, reIssuedRefreshToken));
    }

    private void cacheToken(String accessToken, String refreshToken) {
        redisUtil.set(refreshToken, accessToken);
        redisUtil.expire(refreshToken, JwtProvider.TOKEN_CACHING_SECOND);
    }
}
