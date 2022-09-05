package com.knud4.an.account.dto;

import com.knud4.an.account.entity.Gender;
import com.knud4.an.account.entity.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {
    private Long id;
    private String name;
    private Integer age;
    private Gender gender;
    private String lineName;
    private String houseName;
    private LocalDateTime createdDate;

    public ProfileDTO(Profile profile) {
        this.id = profile.getId();
        this.name = profile.getName();
        this.age = profile.getAge();
        this.gender = profile.getGender();
        this.createdDate = profile.getCreatedDate();

        this.lineName = profile.getAccount().getLineName();
        this.houseName = profile.getAccount().getHouseName();
    }
}
