package com.knud4.an.auth.dto.profile;

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
}
