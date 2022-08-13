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
import com.knud4.an.utils.api.ApiUtil;
import com.knud4.an.utils.api.ApiUtil.ApiSuccessResult;
import com.knud4.an.utils.cookie.CookieUtil;
import com.knud4.an.security.provider.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;

    @PostMapping("/api/v1/auth/account/sign-in")
    public ApiSuccessResult<SignUpAccountResponse> signUpAccount (
            @RequestBody @Valid SignUpAccountForm form) throws RuntimeException {

        Long id = authService.signUpAccount(form);
        return ApiUtil.success(new SignUpAccountResponse(id));
    }

    @PostMapping("/api/v1/auth/account/login")
    public ApiSuccessResult<SignInAccountResponse> signInAccount (
            @RequestBody @Valid SignInAccountForm form,
            HttpServletResponse res) throws RuntimeException {

        Account account = authService.signInAccount(form);

        String accessToken = jwtProvider.generateAccountToken(account.getEmail(), account.getId()+"");

        Cookie cookie = cookieUtil.createCookie(JwtProvider.ACCOUNT_TOKEN_NAME, accessToken);
        res.addCookie(cookie);

        return ApiUtil.success(new SignInAccountResponse
                (account.getId(), account.getLine().getId(), account.getHouse().getId())) ;
    }

    @PostMapping("/api/v1/auth/profile/add")
    public ApiSuccessResult<AddProfileResponse> addProfile(
            @RequestBody @Valid AddProfileForm form,
            HttpServletRequest req) throws RuntimeException {

        String email = getEmailFromCookieToken(req);

        Long profileId = authService.AddProfile(form, email);

        return ApiUtil.success(new AddProfileResponse(profileId));
    }

    @PostMapping("/api/v1/auth/profile/login")
    public ApiSuccessResult<SignInProfileResponse> signInProfile(
            @RequestBody @Valid SignInProfileForm form,
            HttpServletRequest req,
            HttpServletResponse res) throws RuntimeException {


        String email = getEmailFromCookieToken(req);

        Profile profile = authService.signInProfile(form, email);

        String accessToken = jwtProvider.generateProfileToken(profile.getId() + "");
        Cookie cookie = cookieUtil.createCookie(JwtProvider.PROFILE_TOKEN_NAME, accessToken);
        res.addCookie(cookie);

        return ApiUtil.success(new SignInProfileResponse(
                profile.getAccount().getId(), profile.getId(), profile.getName()
        ));
    }

    private String getEmailFromCookieToken(HttpServletRequest req) {
        String access_token = cookieUtil.getCookie(req, "account_token").getValue();
        return jwtProvider.getEmailFromToken(access_token);
    }
}
