package com.knud4.an.auth.dto.manager;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class SignInManagerResponse {

    private Profile profile;
    private Account account;

    @Data
    @AllArgsConstructor
    private static class Profile {
        private String accessToken;
        private String refreshToken;
    }

    @Data
    @AllArgsConstructor
    private static class Account {
        private String accessToken;
        private String refreshToken;
    }

    public SignInManagerResponse(String acAccToken, String acRefToken, String prAccToken, String prRefToken) {
        this.account = new Account(acAccToken, acRefToken);
        this.profile = new Profile(prAccToken, prRefToken);
    }
}
