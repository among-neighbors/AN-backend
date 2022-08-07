package com.knud4.an.auth.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInAccountResponse {
    @NotNull
    private Long accountId;
    @NotNull
    private Long lineId;
    @NotNull
    private Long houseId;
}
