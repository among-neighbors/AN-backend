package com.knud4.an.auth.dto.profile;

import com.knud4.an.account.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddProfileForm {
    @NotNull
    private String name;
    @NotNull
    private Integer age;
    @NotNull
    private String pin;
    @NotNull
    private Gender gender;
}
