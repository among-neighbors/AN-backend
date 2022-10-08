package com.knud4.an.account.controller;

import com.knud4.an.account.dto.AccountDTO;
import com.knud4.an.account.dto.ProfileDTO;
import com.knud4.an.account.dto.ProfileListResponse;
import com.knud4.an.account.entity.Account;
import com.knud4.an.account.entity.Profile;
import com.knud4.an.account.service.AccountService;
import com.knud4.an.annotation.AccountRequired;
import com.knud4.an.annotation.ProfileRequired;
import com.knud4.an.utils.api.ApiUtil;
import com.knud4.an.utils.api.ApiUtil.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Tag(name = "계정/프로필")
@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @AccountRequired
    @Operation(summary = "계정 정보 조회", description = "account token이 필요합니다.")
    @ApiResponse(responseCode = "200", description = "프로필 목록 조회 성공", content = @Content(schema = @Schema(implementation = ProfileListResponse.class)))
    @GetMapping("/api/v1/accounts")
    public ApiSuccessResult<AccountDTO> myAccount(HttpServletRequest req) {
        Account account = accountService.findAccountByAccountId((Long) req.getAttribute("accountId"));

        return ApiUtil.success(new AccountDTO(account));
    }

    @AccountRequired
    @Operation(summary = "프로필 목록 조회", description = "account token이 필요합니다.")
    @ApiResponse(responseCode = "200", description = "프로필 목록 조회 성공", content = @Content(schema = @Schema(implementation = ProfileListResponse.class)))
    @GetMapping("/api/v1/accounts/profiles")
    public ApiSuccessResult<ProfileListResponse> accountProfileList(HttpServletRequest req) throws RuntimeException {
        List<Profile> profiles = accountService
                .findProfilesByAccountId((Long)req.getAttribute("accountId"));

        return ApiUtil.success(new ProfileListResponse(profiles));
    }

    @ProfileRequired
    @Operation(summary = "내 프로필 조회", description = "profile token이 필요합니다.")
    @ApiResponse(responseCode = "200", description = "내 프로필 조회 성공", content = @Content(schema = @Schema(implementation = ProfileDTO.class)))
    @GetMapping("/api/v1/profiles/me")
    public ApiSuccessResult<ProfileDTO> myProfile(HttpServletRequest req) throws RuntimeException {

        Profile profile = accountService.findProfileById((Long)req.getAttribute("profileId"));

        return ApiUtil.success(new ProfileDTO(profile));
    }
}
