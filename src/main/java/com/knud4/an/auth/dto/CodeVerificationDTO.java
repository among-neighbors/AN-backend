package com.knud4.an.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeVerificationDTO {
    @NotNull
    private String email;
    @NotNull
    private String code;
}
