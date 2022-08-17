package com.knud4.an.account.controller;

import com.knud4.an.account.dto.ProfileDTO;
import com.knud4.an.account.dto.ProfileListResponse;
import com.knud4.an.account.entity.Profile;
import com.knud4.an.account.service.AccountService;
import com.knud4.an.utils.api.ApiUtil;
import com.knud4.an.utils.api.ApiUtil.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @Operation(summary = "프로필 목록 조회")
    @GetMapping("/api/v1/account/profiles")
    public ApiSuccessResult<ProfileListResponse> accountProfileList(HttpServletRequest req) throws RuntimeException {
        List<Profile> profiles = accountService
                .findProfilesByAccountId((Long)req.getAttribute("accountId"));

        return ApiUtil.success(new ProfileListResponse(profiles));
    }

    @Operation(summary = "내 프로필 조회")
    @GetMapping("/api/v1/profile/my")
    public ApiSuccessResult<ProfileDTO> myProfile(HttpServletRequest req) throws RuntimeException{
        Profile profile = accountService.findProfileById((Long)req.getAttribute("profileId"));

        return ApiUtil.success(new ProfileDTO(profile.getId(), profile.getName(), profile.getAge(), profile.getGender()));
    }
}
