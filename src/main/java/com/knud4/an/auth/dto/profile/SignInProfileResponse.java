package com.knud4.an.auth.dto.profile;

import com.knud4.an.account.entity.Account;
import com.knud4.an.account.entity.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInProfileResponse {
    @NotNull
    private Long accountId;
    @NotNull
    private Long profileId;
    @NotNull
    private String name;
    @NotNull
    private String accessToken;
    @NotNull String refreshToken;


    public SignInProfileResponse(Profile profile, String accessToken, String refreshToken) {
        this.accountId = profile.getAccount().getId();
        this.profileId = profile.getId();
        this.name = profile.getName();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
