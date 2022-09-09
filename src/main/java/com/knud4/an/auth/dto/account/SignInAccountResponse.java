package com.knud4.an.auth.dto.account;

import com.knud4.an.account.entity.Account;
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
    @NotNull
    private String accessToken;
    @NotNull
    private String refreshToken;

    public SignInAccountResponse(Account account, String accessToken, String refreshToken) {
        this.accountId = account.getId();
        this.lineId = account.getLine().getId();
        this.houseId = account.getHouse().getId();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
