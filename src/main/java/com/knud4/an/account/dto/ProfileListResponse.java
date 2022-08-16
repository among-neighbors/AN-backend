package com.knud4.an.account.dto;

import com.knud4.an.account.entity.Profile;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProfileListResponse {
    private final List<ProfileDTO> profiles;

    public ProfileListResponse(List<Profile> profiles) {
        this.profiles = profiles.stream().map(p -> new ProfileDTO(p.getId(), p.getName(), p.getAge(), p.getGender()))
                .collect(Collectors.toList());
    }
}
