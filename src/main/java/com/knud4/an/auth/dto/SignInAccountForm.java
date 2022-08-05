package com.knud4.an.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInAccountForm {
    @NotNull
    private String username;
    @NotNull
    private String passwd;
}
